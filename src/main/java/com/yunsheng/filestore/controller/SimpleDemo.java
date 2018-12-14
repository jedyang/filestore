package com.yunsheng.filestore.controller;

import com.yunsheng.filestore.entity.UserEntity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SimpleDemo {
    @Autowired
    MongoTemplate mongotemplate;

    @RequestMapping("/query")
    @ResponseBody
    String getInfo() {
        Query query = new Query();
        query.addCriteria(Criteria.where("name").is("酒仙"));
        String name = mongotemplate.findOne(query, UserEntity.class).getUserName();
        return name;
    }

    @RequestMapping("/insert")
    @ResponseBody
    String insert() {
        Query query = new Query();
        query.addCriteria(Criteria.where("name").is("酒仙"));
        String name = mongotemplate.findOne(query, UserEntity.class).getUserName();
        return name;
    }

}
