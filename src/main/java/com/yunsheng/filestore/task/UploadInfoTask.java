package com.yunsheng.filestore.task;

import com.yunsheng.filestore.common.responses.ReturnApi;
import com.yunsheng.filestore.entity.AppDBInfo;
import com.yunsheng.filestore.service.MongoDBService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

/**
 * 昨天的上传数据统计
 */
@Component
@Slf4j
public class UploadInfoTask {

    @Autowired
    MongoDBService mongoDBService;

    // 每天3点
    @Scheduled(cron = "0 0 3 * * ?")
    @Async
    public void yesterdayUpload() {
        log.info("统计昨天各库的上传数量");
        try {
            List<AppDBInfo> allAppDBInfo = mongoDBService.getAllAppDBInfo(0, 100, null);

            for (AppDBInfo appDBInfo : allAppDBInfo) {
                boolean uploadDayInfo = mongoDBService.uploadDayInfo(appDBInfo.getDbName());

                log.info("uploadDayInfo:" + appDBInfo.getDbName() + ",result:" + uploadDayInfo);
            }

        } catch (Exception e) {
            log.error("yesterdayUpload task err:", e);

        }

    }
}
