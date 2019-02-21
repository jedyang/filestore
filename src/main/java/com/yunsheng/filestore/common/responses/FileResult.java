package com.yunsheng.filestore.common.responses;

import java.io.Serializable;

import lombok.Data;

@Data
public class FileResult implements Serializable {
	private static final long serialVersionUID = 2570078432881539466L;

	private boolean success;
	private String msg;
	private String fileUUID;
	private byte[] fileBytes;
	private String fileName;
	private String fileContent;
}
