package com.yunsheng.filestore.service;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoCredential;
import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.yunsheng.filestore.common.CommonDbInfo;
import com.yunsheng.filestore.common.MongoConstant;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Service
public class BaseMongoService {

    @Value("${me.mongoAddress}")
    private String mongoAddress;

    private static final Map<String, MongoClient> mongoClientMap = new HashMap<String, MongoClient>();

    private static final Map<String, MongoTemplate> mongoTemplateMap = new HashMap<>();

    /**
     * @param dbName 要获取的库
     */
    public MongoDatabase getMongoDatabaseByAdmin(String dbName) {
        List<ServerAddress> addressList = getServerAddresses();
        CommonDbInfo commonDbInfo = getCommonDbInfo(addressList, MongoConstant.DB_ADMIN);
        MongoCredential mongoCredential = MongoCredential.createCredential(commonDbInfo.getUser(), commonDbInfo.getDbName(), commonDbInfo.getPwd().toCharArray());
        return getMongoClient(addressList, Arrays.asList(mongoCredential)).getDatabase(dbName);
    }

    /**
     * 获取mongos地址的ServerAddress集合,入参为null,则返回默认地址 host1:port1,host2:port2,...
     */
    public List<ServerAddress> getServerAddresses() {
        List<ServerAddress> addressList = new ArrayList<ServerAddress>();
        String[] addresses = mongoAddress.split(",");
        for (String addr : addresses) {
            String[] hostAndPort = addr.split(":");
            addressList.add(new ServerAddress(hostAndPort[0], Integer.parseInt(hostAndPort[1])));
        }
        return addressList;
    }

    /**
     * 获取commonDB库的commonDB表存储的用户名、密码
     */
    private CommonDbInfo getCommonDbInfo(List<ServerAddress> addressList, String appName) {

        String commonDbKey = addressList.hashCode() + ":commondb";
        MongoClient mongoClient = mongoClientMap.get(commonDbKey);
        if (mongoClient == null) {
            synchronized (BaseMongoService.class) {
                mongoClient = mongoClientMap.get(commonDbKey);
                if (mongoClient == null) {
                    MongoCredential credential = MongoCredential.createCredential(CommonDbInfo.COMMON_USER_NAME, CommonDbInfo.COMMON_DB_NAME, CommonDbInfo.COMMON_PWD.toCharArray());
                    mongoClient = new MongoClient(addressList, Arrays.asList(credential));
                    mongoClient.setReadPreference(ReadPreference.secondaryPreferred());
                    mongoClientMap.put(commonDbKey, mongoClient);
                }
            }
        }
        MongoDatabase commonDBDatabase = mongoClient.getDatabase(CommonDbInfo.COMMON_DB_NAME);
        MongoCollection commonDBCollection = commonDBDatabase.getCollection(CommonDbInfo.COMMON_COLLECTION_NAME);
        Pattern findPattern = Pattern.compile("^.*" + appName + ".*$", Pattern.CASE_INSENSITIVE);
        Object obj = commonDBCollection.find(Filters.regex(CommonDbInfo.KEY_APPKEY, findPattern)).first();
        if (obj == null) {
            return new CommonDbInfo();
        }
        Document document = (Document) obj;
        CommonDbInfo info = new CommonDbInfo();
        info.setUser((String) document.get(CommonDbInfo.KEY_USER));
        info.setPwd((String) document.get(CommonDbInfo.KEY_PWD));
        info.setDbName((String) document.get(CommonDbInfo.KEY_DB_NAME));
        info.setIps((String) document.get(CommonDbInfo.KEY_IPS));
        return info;
    }

    /**
     * 获取某些用户对应的mongoclient
     */
    private MongoClient getMongoClient(List<ServerAddress> addressList, List<MongoCredential> authList) {
        if (addressList == null || addressList.isEmpty()) {
            return new MongoClient();
        }
        String addressesHashCode = String.valueOf(addressList.hashCode());
        String authsHashCode = String.valueOf(authList == null ? 0 : authList.hashCode());
        String mongoClientKey = addressesHashCode + ":" + authsHashCode;
        if (mongoClientMap.get(mongoClientKey) != null) {
            return mongoClientMap.get(mongoClientKey);
        }
        synchronized (BaseMongoService.class) {
            if (mongoClientMap.get(mongoClientKey) != null) {
                return mongoClientMap.get(mongoClientKey);
            }
            MongoClient mongoClient = new MongoClient(addressList, authList);
            mongoClient.setReadPreference(ReadPreference.secondaryPreferred());
            mongoClientMap.put(mongoClientKey, mongoClient);
        }
        return mongoClientMap.get(mongoClientKey);
    }

    public MongoTemplate getMongoTemplate(String userName, String dbName, String passwd) {

        MongoTemplate mongoTemplate = mongoTemplateMap.get(dbName);

        // double check 确保单例
        if (null == mongoTemplate) {
            synchronized (this) {
                mongoTemplate = mongoTemplateMap.get(dbName);
                if (null == mongoTemplate) {
                    StringBuffer sb = new StringBuffer("mongodb://");
                    sb.append(userName).append(":").append(passwd).append("@").append(mongoAddress).append("/").append(dbName);
                    MongoClientURI mongoClientURI = new MongoClientURI(sb.toString());
                    MongoDbFactory mongoDbFactory = new SimpleMongoDbFactory(mongoClientURI);
                    mongoTemplate = new MongoTemplate(mongoDbFactory);
                    mongoTemplateMap.put(dbName, mongoTemplate);
                }
            }
        }

        return mongoTemplate;

    }
}
