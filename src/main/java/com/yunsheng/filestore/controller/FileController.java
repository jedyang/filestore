package com.yunsheng.filestore.controller;

import com.yunsheng.filestore.common.responses.FileRequest;
import com.yunsheng.filestore.common.responses.FileResult;
import com.yunsheng.filestore.service.MongoFileService;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

/**
 * 文件操作
 */
@Slf4j
@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    private MongoFileService mongoFileService;

    /**
     * 直接二进制流下载文件
     * @param request
     * @param response
     * @return
     */
    @GetMapping("/download")
    public String download(HttpServletRequest request, HttpServletResponse response) {
        String appName = request.getParameter("appName");
        String fileId = request.getParameter("fileId");
        FileRequest fileRequest = new FileRequest();
        fileRequest.setAppName(appName);
        fileRequest.setFileId(fileId);
        FileResult fileByUUID = mongoFileService.findFileByUUID(fileRequest);

        OutputStream outputStream = null;
        try {
            byte[] fileBytes = fileByUUID.getFileBytes();
            outputStream = response.getOutputStream();
            outputStream.write(fileBytes);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(outputStream);
        }

        return "success";
    }

}
