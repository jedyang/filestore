package com.yunsheng.filestore.entity;

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

}
