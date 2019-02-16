package com.yunsheng.filestore.service.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.Block;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.yunsheng.filestore.common.MongoConstant;
import com.yunsheng.filestore.entity.AppDBInfo;
import com.yunsheng.filestore.service.BaseMongoService;
import com.yunsheng.filestore.service.MongoDBService;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

@Service("mongoDBService")
@Slf4j
public class MongoDBServiceImpl implements MongoDBService {

    @Autowired
    private BaseMongoService baseMongoService;

    @Override
    public List<AppDBInfo> getAllAppDBInfo(Integer page, Integer limit) {

        List<AppDBInfo> result = new ArrayList<>();
        // 从commonDB获取库的基本信息
        MongoCollection commonDbColl = baseMongoService.getCommonDbColl();
        BasicDBObject filter = new BasicDBObject();
        // 前台传过来的page是从1开始
        FindIterable<Document> findIterable = commonDbColl.find(filter).skip((page - 1) * limit).limit(limit);
        findIterable.forEach(new Block<Document>() {
            @Override
            public void apply(Document o) {
                log.info(o.toJson());
                AppDBInfo one = new AppDBInfo();
                one.setDbName(o.getString("dbName"));
                one.setAppOwner(o.getString("appOwner"));
                // 只返回库名，安全起见不要返回用户名密码
                result.add(one);

            }
        });
        return result;
    }

    @Override
    public long countAllAppDB() {
        MongoCollection commonDbColl = baseMongoService.getCommonDbColl();
        long l = commonDbColl.countDocuments();
        return l;
    }

    @Override
    public AppDBInfo getDbDetail(AppDBInfo appDBInfo) {
        AppDBInfo result = new AppDBInfo();

        MongoDatabase mongoDatabase = baseMongoService.getMongoDatabase(appDBInfo.getDbName());
        Map<String, Object> statsArguments = new BasicDBObject();
        statsArguments.put(MongoConstant.COMMAND_DB_STATS, 1);
        statsArguments.put(MongoConstant.SCALE_KEY, 1024 * 1024);
        BasicDBObject statsCommand = new BasicDBObject(statsArguments);
        Document dbInfo = mongoDatabase.runCommand(statsCommand);
        // dataSize 数据占磁盘的大小，删除document会减小
        // storageSize 数据库占用磁盘的大小。没有压缩的情况下，应该比dataSize大。但是使用wiredTiger开启压缩，这个值一般比dataSize小。删除document，不会减小
        // fileSize 最大，是storageSize再加上索引，删除document和collection都不会减小
        result.setDataSize(String.valueOf(dbInfo.get("dataSize")));
        result.setStorageSize(String.valueOf(dbInfo.get("storageSize")));
        result.setFileSize(String.valueOf(dbInfo.get("fileSize")));

        return result;
    }
}
