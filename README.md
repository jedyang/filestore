# filestore
使用mongoDB做后端存储的文件管理系统。

用户对mongo的连接信息存放在commonDB表中。
用户只需要一个appname即可上传下载文件。

# 数据库索引
申请表对appName加唯一索引：
db.applyInfo.createIndex({ "appName": 1 }, { unique: true });

用户表对username加唯一索引:
db.userInfo.createIndex({ "appName": 1 }, { unique: true });

数据库连接信息表加唯一索引
db.commonDB.createIndex({ "dbName": 1 }, { unique: true })