package com.yunsheng.filestore.service.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.Block;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.UpdateResult;
import com.yunsheng.filestore.entity.ApplyInfo;
import com.yunsheng.filestore.service.ApplyService;
import com.yunsheng.filestore.service.BaseMongoService;
import com.yunsheng.filestore.util.BeanUtil;

import org.apache.commons.lang.StringUtils;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.management.Query;

@Service("applyService")
public class ApplyServiceImpl implements ApplyService {

    @Autowired
    private BaseMongoService baseMongoService;

    @Override
    public int insertApply(ApplyInfo applyInfo) {
        MongoDatabase commonDbDababase = baseMongoService.getCommonDbDababase();
        MongoCollection<Document> applyInfoCol = commonDbDababase.getCollection("applyInfo");

        try {
            Document document = BeanUtil.bean2Document(applyInfo);
            applyInfoCol.insertOne(document);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return 0;
        }
        return 1;
    }

    @Override
    public List<ApplyInfo> queryApplyInfo(String applyName) {
        List<ApplyInfo> result = new ArrayList<>();

        MongoDatabase commonDbDababase = baseMongoService.getCommonDbDababase();
        MongoCollection<Document> applyInfoCol = commonDbDababase.getCollection("applyInfo");

        BasicDBObject filter = new BasicDBObject();
        if (StringUtils.isNotBlank(applyName)){
            filter.put("applyName", applyName);
        }
        FindIterable<Document> documents = applyInfoCol.find(filter);

        documents.forEach(new Block<Document>() {
            @Override
            public void apply(Document document) {
                ApplyInfo applyInfo = new ApplyInfo();
                try {
                    BeanUtil.dbObject2Bean(document, applyInfo);
                    result.add(applyInfo);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
        });
        return result;
    }

    @Override
    public long updateApply(ApplyInfo applyInfo) {
        MongoDatabase commonDbDababase = baseMongoService.getCommonDbDababase();
        MongoCollection<Document> applyInfoCol = commonDbDababase.getCollection("applyInfo");

        UpdateResult updateResult = applyInfoCol.updateOne(Filters.eq("_id", new ObjectId(applyInfo.getId())),
                new Document("$set", new Document("status", applyInfo.getStatus())));

        return updateResult.getModifiedCount();
    }
}
