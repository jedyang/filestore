package com.yunsheng.filestore.service.impl;

import com.yunsheng.filestore.entity.ApplyInfo;
import com.yunsheng.filestore.service.ApplyService;

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
public class ApplyServiceImplTest {
    @Autowired
    ApplyService applyService;

    @Test
    public void insertApply() throws Exception {
    }

    @Test
    public void queryApplyInfo() throws Exception {
    }

    @Test
    public void updateApply() throws Exception {
        ApplyInfo applyInfo = new ApplyInfo();
        applyInfo.setAppName("paa1");
        applyInfo.setId("5c80c5ff692a8522c809cb48");
        applyInfo.setStatus(10);
        applyService.updateApply(applyInfo);
    }

}