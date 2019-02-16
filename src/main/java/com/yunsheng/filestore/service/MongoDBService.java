package com.yunsheng.filestore.service;

import com.yunsheng.filestore.entity.AppDBInfo;

import java.util.List;

public interface MongoDBService {

    List<AppDBInfo> getAllAppDBInfo(Integer page, Integer limit);

    long countAllAppDB();

    // 获取一个库的详细信息
    AppDBInfo getDbDetail(AppDBInfo appDBInfo);
}
