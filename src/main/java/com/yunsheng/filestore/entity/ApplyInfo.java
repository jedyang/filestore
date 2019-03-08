package com.yunsheng.filestore.entity;

import lombok.Data;

@Data
public class ApplyInfo {
    private String id;

    private String appName;

    private String platform;
    private String teamName;
    private String userName;
    private String userEmail;
    private String userPhone;
    private int space;
    private int storeLife;
    private String desc;
    private String applyName; //申请提交人

    // 0:申请中，1：通过  2：拒绝
    private int status;
    // mongo连接地址
    private String ips;


}
