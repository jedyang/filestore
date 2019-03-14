package com.yunsheng.filestore.task;

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
public class UploadInfoTaskTest {

    @Autowired
    UploadInfoTask uploadInfoTask;

    @Test
    public void yesterdayUpload() throws Exception {
//        uploadInfoTask.yesterdayUpload();
    }

}