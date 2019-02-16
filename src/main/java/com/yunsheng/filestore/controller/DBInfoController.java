package com.yunsheng.filestore.controller;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoSecurityException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.yunsheng.filestore.common.CommonDbInfo;
import com.yunsheng.filestore.common.MongoConstant;
import com.yunsheng.filestore.common.responses.ApiResponses;
import com.yunsheng.filestore.common.responses.ErrorCodeEnum;
import com.yunsheng.filestore.entity.AppDBInfo;
import com.yunsheng.filestore.entity.CommonDB;
import com.yunsheng.filestore.service.BaseMongoService;
import com.yunsheng.filestore.service.MongoDBService;

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
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class DBInfoController {

    @Autowired
    MongoDBService mongoDBService;

    @RequestMapping(value = "/pages/filestore/{page}")
    public ModelAndView filestore(@PathVariable("page") String page) {
        log.info("6show:" + page);

        ModelAndView view = new ModelAndView();

        view.setViewName("/pages/filestore/" + page);

//        List<AppDBInfo> allAppDBInfo = mongoDBService.getAllAppDBInfo();
//
//        view.addObject("allAppDBInfo", allAppDBInfo);
        return view;
    }


    /**
     * 返回库的信息
     */
    @RequestMapping("/dbinfo/queryInfo")
    @ResponseBody
    public ApiResponses<List<AppDBInfo>> queryInfo(@RequestParam(name = "page") Integer page, @RequestParam(name = "limit") Integer limit) {

        List<AppDBInfo> allAppDBInfo;
        long count = 0;
        try {
            allAppDBInfo = mongoDBService.getAllAppDBInfo(page, limit);
            count = mongoDBService.countAllAppDB();

            for (AppDBInfo appDBInfo : allAppDBInfo) {

                AppDBInfo detail = null;
                try {
                    detail = mongoDBService.getDbDetail(appDBInfo);
                } catch (Exception e) {
                    e.printStackTrace();
                    continue;
                }
                appDBInfo.setStorageSize(detail.getStorageSize());
                appDBInfo.setDataSize(detail.getDataSize());
                appDBInfo.setFileSize(detail.getFileSize());
            }

        } catch (Exception e) {
            log.error(e.getMessage());
            ApiResponses<List<AppDBInfo>> responses = new ApiResponses<>();
            responses.setCode(1);
            responses.setMsg("fail");
            return responses;

        }
        ApiResponses<List<AppDBInfo>> responses = new ApiResponses<>();
        responses.setCode(0);
        responses.setMsg("success");
        responses.setData(allAppDBInfo);
        responses.setCount(count);
        return responses;
    }
//    /**
//     * 返回库的信息
//     */
//    @RequestMapping("/dbinfo/queryInfo")
//    @ResponseBody
//    public ApiResponses<Document> queryInfo(@RequestParam String user, @RequestParam String dbName, @RequestParam String pwd) {
//
//        Document dbInfo;
//        try {
//            MongoTemplate myTemplate = null;
//
//            MongoDatabase mongoDatabase = myTemplate.getDb();
//            Map<String, Object> statsArguments = new BasicDBObject();
//            statsArguments.put(MongoConstant.COMMAND_DB_STATS, 1);
//            statsArguments.put(MongoConstant.SCALE_KEY, 1024 * 1024);
//            BasicDBObject statsCommand = new BasicDBObject(statsArguments);
//            dbInfo = mongoDatabase.runCommand(statsCommand);
//            log.info(dbInfo.toJson());
//        } catch (Exception e) {
//            return ApiResponses.failure(ErrorCodeEnum.UNKNOW_ERR, e);
//
//        }
//
//        return ApiResponses.success(dbInfo);
//    }


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
