package com.yunsheng.filestore.common.responses;

import java.io.Serializable;

import lombok.Data;

@Data
public class FileRequest implements Serializable {

    private static final long serialVersionUID = 8733379055240949783L;
    private byte[] bytes;
    private String fileName;
    private String appName;

}