package com.yunsheng.filestore.entity;

import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class AppDBInfo {

    private String user;
    private String dbName;
    private String ips;
    private String appOwner;
    private String dataSize;
    private String storageSize;
    private String fileSize;

    // 库下的表信息
    private List<Map> collInfos;
}
