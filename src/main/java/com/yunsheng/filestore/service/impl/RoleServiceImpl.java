package com.yunsheng.filestore.service.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.Block;
import com.mongodb.MongoWriteException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.yunsheng.filestore.entity.Role;
import com.yunsheng.filestore.entity.User;
import com.yunsheng.filestore.service.BaseMongoService;
import com.yunsheng.filestore.service.RoleService;
import com.yunsheng.filestore.util.BeanUtil;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;

@Service("roleService")
public class RoleServiceImpl implements RoleService {
    @Autowired
    private BaseMongoService baseMongoService;

    @Override
    public Set<String> getRoles(String username) {
        Set<String> result = new HashSet<>();

        MongoDatabase commonDbDababase = baseMongoService.getCommonDbDababase();
        MongoCollection<Document> userInfo = commonDbDababase.getCollection("roleInfo");

        BasicDBObject filter = new BasicDBObject();
        filter.append("username", username);
        FindIterable<Document> documents = userInfo.find(filter);
        documents.forEach(new Block<Document>() {
            @Override
            public void apply(Document document) {
                result.add(document.getString("rolename"));
            }
        });

        return result;
    }

    @Override
    public int insertRole(Role role) {
        MongoDatabase commonDbDababase = baseMongoService.getCommonDbDababase();
        MongoCollection<Document> userInfo = commonDbDababase.getCollection("roleInfo");

        Document doc = null;
        try {
            doc = BeanUtil.bean2Document(role);
            userInfo.insertOne(doc);
        } catch (IllegalAccessException e) {
            return 0;
        } catch (MongoWriteException e){
            int code = e.getError().getCode();
            return code;
        }
        return 1;
    }
}
