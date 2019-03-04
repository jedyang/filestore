package com.yunsheng.filestore.service;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoCredential;
import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.yunsheng.filestore.common.CommonDbInfo;
import com.yunsheng.filestore.common.MongoConstant;

import org.apache.commons.lang.StringUtils;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BaseMongoService {

    @Value("${me.mongoAddress}")
    private String mongoAddress;

    private static final Map<String, MongoClient> mongoClientMap = new HashMap<String, MongoClient>();

//    private static final Map<String, MongoTemplate> mongoTemplateMap = new HashMap<>();

    /**
     * 获取db对象
     */
    public DB getDB(String dbName) {
        CommonDbInfo commonDbInfo = getCommonDbInfo(dbName);
        if (StringUtils.isBlank(commonDbInfo.getUser())) {
            log.error("common表中未记录该库信息");
        }
        MongoCredential mongoCredential = MongoCredential.createCredential(commonDbInfo.getUser(), commonDbInfo.getDbName(), commonDbInfo.getPwd().toCharArray());

        List<ServerAddress> addressList = getServerAddresses(commonDbInfo.getIps());

        MongoClient mongoClient = getMongoClient(addressList, mongoCredential);

        return mongoClient.getDB(dbName);
    }

    /**
     * 获取commonDB库的连接
     */
    public MongoDatabase getCommonDbDababase() {

        List<ServerAddress> addressList = getServerAddresses("");

        String commonDbKey = addressList.hashCode() + ":commondb";
        MongoClient mongoClient = mongoClientMap.get(commonDbKey);
        if (mongoClient == null) {
            synchronized (BaseMongoService.class) {
                mongoClient = mongoClientMap.get(commonDbKey);
                if (mongoClient == null) {
                    MongoCredential credential = MongoCredential.createCredential(CommonDbInfo.COMMON_USER_NAME, CommonDbInfo.COMMON_DB_NAME, CommonDbInfo.COMMON_PWD.toCharArray());
                    MongoClientOptions options = MongoClientOptions.builder().connectionsPerHost(50).build();
                    mongoClient = new MongoClient(addressList, credential, options);
                    mongoClientMap.put(commonDbKey, mongoClient);
                }
            }
        }
        MongoDatabase commonDBDatabase = mongoClient.getDatabase(CommonDbInfo.COMMON_DB_NAME);

        return commonDBDatabase;
    }




    /**
     * @param dbName 要获取的库
     */
    public MongoDatabase getMongoDatabase(String dbName) {

        CommonDbInfo commonDbInfo = getCommonDbInfo(dbName);
        MongoCredential mongoCredential = MongoCredential.createCredential(commonDbInfo.getUser(), commonDbInfo.getDbName(), commonDbInfo.getPwd().toCharArray());
        String ips = commonDbInfo.getIps();

        MongoClient mongoClient = getMongoClient(getServerAddresses(ips), mongoCredential);

        return mongoClient.getDatabase(dbName);
    }

    /**
     * 获取mongos地址的ServerAddress集合,入参为null,则返回默认地址 host1:port1,host2:port2,...
     */
    private List<ServerAddress> getServerAddresses(String addressStr) {
        // 如果为空，使用默认的
        if (StringUtils.isBlank(addressStr)) {
            addressStr = mongoAddress;
        }
        List<ServerAddress> addressList = new ArrayList<ServerAddress>();
        String[] addresses = addressStr.split(",");
        for (String addr : addresses) {
            String[] hostAndPort = addr.split(":");
            addressList.add(new ServerAddress(hostAndPort[0], Integer.parseInt(hostAndPort[1])));
        }
        return addressList;
    }

    /**
     * 获取commonDB库的commonDB表存储的用户名、密码
     */
    private CommonDbInfo getCommonDbInfo(String appName) {

        List<ServerAddress> addressList = getServerAddresses("");

        String commonDbKey = addressList.hashCode() + ":commondb";
        MongoClient mongoClient = mongoClientMap.get(commonDbKey);
        if (mongoClient == null) {
            synchronized (BaseMongoService.class) {
                mongoClient = mongoClientMap.get(commonDbKey);
                if (mongoClient == null) {
                    MongoCredential credential = MongoCredential.createCredential(CommonDbInfo.COMMON_USER_NAME, CommonDbInfo.COMMON_DB_NAME, CommonDbInfo.COMMON_PWD.toCharArray());
                    MongoClientOptions options = MongoClientOptions.builder().connectionsPerHost(50).build();
                    mongoClient = new MongoClient(addressList, credential, options);
                    mongoClientMap.put(commonDbKey, mongoClient);
                }
            }
        }
        MongoDatabase commonDBDatabase = mongoClient.getDatabase(CommonDbInfo.COMMON_DB_NAME);
        MongoCollection commonDBCollection = commonDBDatabase.getCollection(CommonDbInfo.COMMON_COLLECTION_NAME);
        BasicDBObject filter = new BasicDBObject();
        filter.put("dbName", appName);
        Object obj = commonDBCollection.find(filter).first();
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
    private MongoClient getMongoClient(List<ServerAddress> addressList, MongoCredential auth) {
        if (addressList == null || addressList.isEmpty()) {
            return new MongoClient();
        }
        String addressesHashCode = String.valueOf(addressList.hashCode());
        String authsHashCode = String.valueOf(auth);
        String mongoClientKey = addressesHashCode + ":" + authsHashCode;
        if (mongoClientMap.get(mongoClientKey) != null) {
            return mongoClientMap.get(mongoClientKey);
        }
        synchronized (BaseMongoService.class) {
            if (mongoClientMap.get(mongoClientKey) != null) {
                return mongoClientMap.get(mongoClientKey);
            }
            MongoClientOptions options = MongoClientOptions.builder().connectionsPerHost(50).build();
            MongoClient mongoClient = new MongoClient(addressList, auth, options);
            mongoClientMap.put(mongoClientKey, mongoClient);
        }
        return mongoClientMap.get(mongoClientKey);
    }

//    public MongoTemplate getMongoTemplate(String userName, String dbName, String passwd) {
//
//        MongoTemplate mongoTemplate = mongoTemplateMap.get(dbName);
//
//        // double check 确保单例
//        if (null == mongoTemplate) {
//            synchronized (this) {
//                mongoTemplate = mongoTemplateMap.get(dbName);
//                if (null == mongoTemplate) {
//                    StringBuffer sb = new StringBuffer("mongodb://");
//                    sb.append(userName).append(":").append(passwd).append("@").append(mongoAddress).append("/").append(dbName);
//                    MongoClientURI mongoClientURI = new MongoClientURI(sb.toString());
//                    MongoDbFactory mongoDbFactory = new SimpleMongoDbFactory(mongoClientURI);
//                    mongoTemplate = new MongoTemplate(mongoDbFactory);
//                    mongoTemplateMap.put(dbName, mongoTemplate);
//                }
//            }
//        }
//
//        return mongoTemplate;
//
//    }
}
