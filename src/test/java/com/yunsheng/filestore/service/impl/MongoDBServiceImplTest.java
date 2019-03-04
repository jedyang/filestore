package com.yunsheng.filestore.service.impl;

import com.yunsheng.filestore.entity.AppDBInfo;
import com.yunsheng.filestore.service.MongoDBService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import lombok.extern.slf4j.Slf4j;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {com.yunsheng.filestore.FilestoreApplication.class})
@Slf4j
public class MongoDBServiceImplTest {
    @Autowired
    private MongoDBService mongoDBService;

    @Test
    public void uploadDayInfo() throws Exception {
        mongoDBService.uploadDayInfo("showcase");
    }

    @Test
    public void getDbDetail() throws Exception {
        AppDBInfo showcase = mongoDBService.getDbDetail("showcase");
        log.info(showcase.toString());
    }

}