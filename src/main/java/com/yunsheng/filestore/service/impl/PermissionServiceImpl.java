package com.yunsheng.filestore.service.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.Block;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.yunsheng.filestore.service.BaseMongoService;
import com.yunsheng.filestore.service.PermissionService;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service("permissionService")
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private BaseMongoService baseMongoService;

    @Override
    public Set<String> getPermissions(String username) {
        Set<String> result = new HashSet<>();

        MongoDatabase commonDbDababase = baseMongoService.getCommonDbDababase();
        MongoCollection<Document> permissionInfo = commonDbDababase.getCollection("permissionInfo");

        BasicDBObject filter = new BasicDBObject();
        filter.append("username", username);
        FindIterable<Document> documents = permissionInfo.find(filter);
        documents.forEach(new Block<Document>() {
            @Override
            public void apply(Document document) {
                result.add(document.getString("permission"));
            }
        });

        return result;
    }
}
