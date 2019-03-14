package com.yunsheng.filestore.service;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.yunsheng.filestore.common.CommonDbInfo;

import org.bson.Document;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {com.yunsheng.filestore.FilestoreApplication.class})
@Slf4j
public class BaseMongoServiceTest {
    @Autowired
    BaseMongoService baseMongoService;

    @Test
    public void createUser() throws Exception {
//        baseMongoService.createUser("", "passtest", "passtest", "passtest");
    }

    @Test
    public void testCommonDB(){
        List<ServerAddress> addressList = baseMongoService.getServerAddresses("");

        MongoCredential credential = MongoCredential.createCredential(CommonDbInfo.COMMON_USER_NAME, CommonDbInfo.COMMON_DB_NAME, CommonDbInfo.COMMON_PWD.toCharArray());
        MongoClientOptions options = MongoClientOptions.builder().connectionsPerHost(50).build();
        MongoClient mongoClient = new MongoClient(addressList, credential, options);
        MongoDatabase commonDBDatabase = mongoClient.getDatabase(CommonDbInfo.COMMON_DB_NAME);


        MongoCollection<Document> collection = commonDBDatabase.getCollection(CommonDbInfo.COMMON_COLLECTION_NAME);

        long count = collection.count();

        log.info("l:" + count);


    }

}