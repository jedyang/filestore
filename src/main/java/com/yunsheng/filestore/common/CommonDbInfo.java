package com.yunsheng.filestore.common;

public class CommonDbInfo {

	public static final String COMMON_DB_NAME = "commonDB";
	public static final String COMMON_COLLECTION_NAME = "commonDB";
	public static final String COMMON_USER_NAME = "common";
	public static final String COMMON_PWD = "123456";

	public static final String KEY_APPKEY = "appKey";
	public static final String KEY_USER = "user";
	public static final String KEY_PWD = "pwd";
	public static final String KEY_DB_NAME = "dbName";
	public static final String KEY_IPS = "ips";
	public static final String KEY_MAX_SIZE = "maxSize";
	
	private String user;
	private String pwd;
	private String dbName;
	private String ips;
	
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getDbName() {
		return dbName;
	}
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}
	public String getIps() {
		return ips;
	}
	public void setIps(String ips) {
		this.ips = ips;
	}
}
