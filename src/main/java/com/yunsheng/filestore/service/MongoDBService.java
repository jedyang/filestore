package com.yunsheng.filestore.service;

import com.yunsheng.filestore.entity.AppDBInfo;

import java.util.List;
import java.util.Map;

public interface MongoDBService {

    List<AppDBInfo> getAllAppDBInfo(Integer page, Integer limit);

    long countAllAppDB();

    // 获取一个库的详细信息
    AppDBInfo getDbDetail(String dbName);

    // 查询库的表信息
    Map<String, String> getCollectionInfo(AppDBInfo appDBInfo);

    // 保存每天新增的数据量
    boolean uploadDayInfo(String dbName);

    // 获取曲线图所需数据
    Map<String, List<String>> getChartData(String dbName);
}
