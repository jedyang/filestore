package com.yunsheng.filestore.service;

import com.yunsheng.filestore.entity.AppDBInfo;

import java.util.List;
import java.util.Map;

public interface MongoDBService {

    List<AppDBInfo> getAllAppDBInfo(Integer page, Integer limit);

    long countAllAppDB();

    // 获取一个库的详细信息
    AppDBInfo getDbDetail(AppDBInfo appDBInfo);

    // 查询库的表信息
    Map<String, String> getCollectionInfo(AppDBInfo appDBInfo);
}
