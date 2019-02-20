package com.yunsheng.filestore.common;

import java.io.Serializable;

import lombok.Data;

@Data
public class CommonDbInfo implements Serializable{
	private static final long serialVersionUID = -3258839839160856613L;

	public static final String COMMON_DB_NAME = "commonDB";
	public static final String COMMON_COLLECTION_NAME = "commonDB";
	public static final String COMMON_USER_NAME = "common";
	public static final String COMMON_PWD = "passwd";

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
	
}
