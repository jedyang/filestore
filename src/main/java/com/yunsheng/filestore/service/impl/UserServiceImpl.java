package com.yunsheng.filestore.service.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoWriteException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.yunsheng.filestore.entity.User;
import com.yunsheng.filestore.service.BaseMongoService;
import com.yunsheng.filestore.service.UserService;
import com.yunsheng.filestore.util.BeanUtil;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;

@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private BaseMongoService baseMongoService;

    @Override
    public int insertUser(User one) {
        // user表放在commonDB下
        MongoDatabase commonDbDababase = baseMongoService.getCommonDbDababase();
        MongoCollection<Document> userInfo = commonDbDababase.getCollection("userInfo");

        Document doc = null;
        try {
            doc = BeanUtil.bean2Document(one);
            userInfo.insertOne(doc);
            MongoCollection<Document> roleInfo = commonDbDababase.getCollection("roleInfo");
            // 注册用户默认为user角色
            roleInfo.insertOne(new Document().append("username", one.getUsername()).append("rolename", "user"));
        } catch (IllegalAccessException e) {
            return 0;
        } catch (MongoWriteException e) {
            int code = e.getError().getCode();
            return code;
        }
        return 1;
    }

    @Override
    public User getUserByUsername(String username) {
        MongoDatabase commonDbDababase = baseMongoService.getCommonDbDababase();
        MongoCollection<Document> userInfo = commonDbDababase.getCollection("userInfo");

        BasicDBObject filter = new BasicDBObject();
        filter.append("username", username);
        FindIterable<Document> documents = userInfo.find(filter);
        Document first = documents.first();
        if (null != first) {
            try {
                User user = new User();
                BeanUtil.dbObject2Bean(first, user);
                return user;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
