package com.yunsheng.filestore.controller;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.yunsheng.filestore.common.CommonDbInfo;
import com.yunsheng.filestore.common.MongoConstant;
import com.yunsheng.filestore.common.responses.ApiResponses;
import com.yunsheng.filestore.common.responses.ErrorCodeEnum;
import com.yunsheng.filestore.entity.CommonDB;
import com.yunsheng.filestore.service.BaseMongoService;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.data.mongodb.core.query.Criteria;
//import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/dbInfo")
@Slf4j
public class DBInfoController {
    @Autowired
    MongoTemplate mongotemplate;

    @Autowired
    BaseMongoService baseMongoService;

    /**
     * 返回库的信息
     */
    @RequestMapping("/queryAll")
    public ApiResponses<List<CommonDB>> queryAll() {
        List<CommonDB> mongotemplateAll = new ArrayList<>();
        try {

            mongotemplateAll = mongotemplate.findAll(CommonDB.class);
        } catch (Exception e) {
            return ApiResponses.failure(ErrorCodeEnum.UNKNOW_ERR, e);
        }

        return ApiResponses.success(mongotemplateAll);
    }

    /**
     * 返回库的信息
     */
    @RequestMapping("/queryInfo")
    public ApiResponses<Document> queryInfo(@RequestParam String user,@RequestParam String dbName, @RequestParam String pwd) {
        Document dbInfo;
        try {
//            Query query = new Query();
//            query.addCriteria(Criteria.where("dbName").is(dbName));
//            dbInfo = mongotemplate.findOne(query, CommonDB.class);

            MongoTemplate myTemplate = baseMongoService.getMongoTemplate(user, dbName, pwd);

            MongoDatabase mongoDatabase = myTemplate.getDb();
            Map<String, Object> statsArguments = new BasicDBObject();
            statsArguments.put(MongoConstant.COMMAND_DB_STATS, 1);
            statsArguments.put(MongoConstant.SCALE_KEY, 1024 * 1024);
            BasicDBObject statsCommand = new BasicDBObject(statsArguments);
            dbInfo = mongoDatabase.runCommand(statsCommand);
//            MongoCollection<Document> collection = myTemplate.getCollection("fs.files");

//            long l = collection.countDocuments();
            log.info(dbInfo.toJson());
        } catch (Exception e) {
            return ApiResponses.failure(ErrorCodeEnum.UNKNOW_ERR, e);

        }

        return ApiResponses.success(dbInfo);
    }


    /**
     * 返回库的信息
     * @return
     */
//    @RequestMapping("/query")
//    @ResponseBody
//    public Integer getInfo() {
//        MongoDatabase mongoDatabase = baseMongoService.getMongoDatabaseByAdmin("paas_test");
//        Map<String, Object> statsArguments = new BasicDBObject();
//        statsArguments.put(MongoConstant.COMMAND_DB_STATS, 1);
//        statsArguments.put(MongoConstant.SCALE_KEY, 1024*1024);
//        BasicDBObject statsCommand = new BasicDBObject(statsArguments);
//        Document statsDocument = mongoDatabase.runCommand(statsCommand);
//        if (statsDocument == null){
//            return 0;
//        }
//        return statsDocument.getInteger(MongoConstant.RESULT_STORAGE_SIZE);
//    }


}
