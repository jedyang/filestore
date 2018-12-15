package com.yunsheng.filestore.controller;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoDatabase;
import com.yunsheng.filestore.common.MongoConstant;
import com.yunsheng.filestore.entity.UserEntity;
import com.yunsheng.filestore.service.BaseMongoService;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.data.mongodb.core.query.Criteria;
//import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class SimpleDemo {
//    @Autowired
//    MongoTemplate mongotemplate;

    @Autowired
    BaseMongoService baseMongoService;

    /**
     * 返回库的信息
     * @return
     */
    @RequestMapping("/query")
    @ResponseBody
    public Integer getInfo() {
        MongoDatabase mongoDatabase = baseMongoService.getMongoDatabaseByAdmin("paas_test");
        Map<String, Object> statsArguments = new BasicDBObject();
        statsArguments.put(MongoConstant.COMMAND_DB_STATS, 1);
        statsArguments.put(MongoConstant.SCALE_KEY, 1024*1024);
        BasicDBObject statsCommand = new BasicDBObject(statsArguments);
        Document statsDocument = mongoDatabase.runCommand(statsCommand);
        if (statsDocument == null){
            return 0;
        }
        return statsDocument.getInteger(MongoConstant.RESULT_STORAGE_SIZE);
    }


}
