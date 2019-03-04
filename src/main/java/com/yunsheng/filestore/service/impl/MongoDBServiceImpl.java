package com.yunsheng.filestore.service.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.Block;
import com.mongodb.MongoSecurityException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Sorts;
import com.yunsheng.filestore.common.CommonDbInfo;
import com.yunsheng.filestore.common.MongoConstant;
import com.yunsheng.filestore.entity.AppDBInfo;
import com.yunsheng.filestore.service.BaseMongoService;
import com.yunsheng.filestore.service.MongoDBService;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
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
        MongoDatabase commonDBDatabase = baseMongoService.getCommonDbDababase();
        MongoCollection commonDBCollection = commonDBDatabase.getCollection(CommonDbInfo.COMMON_COLLECTION_NAME);

        BasicDBObject filter = new BasicDBObject();
        // 前台传过来的page是从1开始
        FindIterable<Document> findIterable = commonDBCollection.find(filter).skip((page - 1) * limit).limit(limit);
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
        MongoDatabase commonDBDatabase = baseMongoService.getCommonDbDababase();
        MongoCollection commonDBCollection = commonDBDatabase.getCollection(CommonDbInfo.COMMON_COLLECTION_NAME);
        long l = commonDBCollection.countDocuments();
        return l;
    }

    @Override
    public AppDBInfo getDbDetail(String dbName) {
        AppDBInfo result = new AppDBInfo();
        result.setDbName(dbName);

        MongoDatabase mongoDatabase = baseMongoService.getMongoDatabase(dbName);
        Map<String, Object> statsArguments = new BasicDBObject();
        statsArguments.put(MongoConstant.COMMAND_DB_STATS, 1);
        statsArguments.put(MongoConstant.SCALE_KEY, 1024 * 1024);
        BasicDBObject statsCommand = new BasicDBObject(statsArguments);
        Document dbInfo = mongoDatabase.runCommand(statsCommand);
        // dataSize 数据占磁盘的大小，删除document会减小
        // storageSize 数据库占用磁盘的大小。没有压缩的情况下，应该比dataSize大。但是使用wiredTiger开启压缩，这个值一般比dataSize小。删除document，不会减小
        result.setDataSize(String.valueOf(dbInfo.get("dataSize")));
        result.setStorageSize(String.valueOf(dbInfo.get("storageSize")));

        MongoCollection<Document> filesColl = mongoDatabase.getCollection("fs.files");
        // 文件总数
        long filesCount = filesColl.count();
        // 昨日新增
        SimpleDateFormat sdfDay = new SimpleDateFormat("yyyy-MM-dd");
        String today = sdfDay.format(new Date());
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        calendar.add(calendar.DATE, -1);
        String startDate = sdfDay.format(calendar.getTime()) + " 08:00:00";
        String endDate = today + " 08:00:00";

        BasicDBObject filter = new BasicDBObject();
        BasicDBObject filterDate = new BasicDBObject();
        filterDate.put("$gte", startDate);
        filterDate.put("$lte", endDate);
        filter.put("uploadDate", filterDate);
        long yesterdayCount = filesColl.count(filter);

        result.setCount(filesCount);
        result.setYesterDayCount(yesterdayCount);

        // 最早上传
        FindIterable<Document> filesIte = filesColl.find().sort(Sorts.ascending("uploadDate")).limit(1);
        Document first = filesIte.first();
        if (null != first && null != first.get("uploadDate")) {
            result.setEarliest(first.get("uploadDate").toString());
        }

        return result;
    }

    @Override
    public Map<String, String> getCollectionInfo(AppDBInfo appDBInfo) {
        Map<String, String> result = new HashMap<>();
        try {
            MongoDatabase mongoDatabase = baseMongoService.getMongoDatabase(appDBInfo.getDbName());
            MongoCollection<Document> filesColl = mongoDatabase.getCollection("fs.files");
            long filesCount = filesColl.count();

            MongoCollection<Document> chunksColl = mongoDatabase.getCollection("fs.chunks");
            long chunksCount = chunksColl.count();

            result.put("fs.files", String.valueOf(filesCount));
            result.put("fs.chunks", String.valueOf(chunksCount));
        } catch (MongoSecurityException e) {
            log.error("数据库鉴权失败:" + appDBInfo.getDbName());
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return result;
    }

    @Override
    public boolean uploadDayInfo(String dbName) {

        try {
            MongoDatabase mongoDatabase = baseMongoService.getMongoDatabase(dbName);
            MongoCollection<Document> filesColl = mongoDatabase.getCollection("fs.files");

            // 昨日新增
            SimpleDateFormat sdfDay = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(new Date());
            calendar.add(calendar.DATE, -1);
            String yesterDay = sdfDay.format(calendar.getTime());
            String startDate = yesterDay + " 08:00:00";
            String today = sdfDay.format(new Date());
            String endDate = today + " 08:00:00";

            BasicDBObject filter = new BasicDBObject();
            BasicDBObject filterDate = new BasicDBObject();
            filterDate.put("$gte", startDate);
            filterDate.put("$lte", endDate);
            filter.put("uploadDate", filterDate);
            long yesterdayCount = filesColl.count(filter);

            Document one = new Document();
            one.append("dbName", dbName)
                    .append("createdAt", new Date())
                    .append("date", yesterDay)
                    .append("added", yesterdayCount);

            MongoDatabase commonDBDatabase = baseMongoService.getCommonDbDababase();
            MongoCollection<Document> uploadIndo = commonDBDatabase.getCollection("uploadInfo");

            uploadIndo.insertOne(one);
        } catch (Exception e) {
            log.error("存储每日上传统计信息失败", e);
            return false;
        }

        return true;
    }

    @Override
    public Map<String, List<String>> getChartData(String dbName) {
        Map<String, List<String>> result = new HashMap<>();

        MongoDatabase commonDBDatabase = baseMongoService.getCommonDbDababase();
        MongoCollection<Document> uploadIndo = commonDBDatabase.getCollection("uploadInfo");
        BasicDBObject filter = new BasicDBObject();
        filter.put("dbName", dbName);
        FindIterable<Document> documents = uploadIndo.find(filter);
        List<String> dates = new ArrayList<>();
        List<String> counts = new ArrayList<>();
        documents.forEach(new Block<Document>() {
            @Override
            public void apply(Document o) {
                dates.add(o.getString("date"));
                counts.add(o.get("added").toString());
            }
        });

        result.put("xAxis", dates);
        result.put("series", counts);
        return result;
    }
}
