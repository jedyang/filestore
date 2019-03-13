package com.yunsheng.filestore.service;

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
public class BaseMongoServiceTest {
    @Autowired
    BaseMongoService baseMongoService;

    @Test
    public void createUser() throws Exception {
        baseMongoService.createUser("", "passtest", "passtest", "passtest");
    }

}