package com.yunsheng.filestore.service.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;
import com.yunsheng.filestore.common.responses.FileRequest;
import com.yunsheng.filestore.common.responses.FileResult;
import com.yunsheng.filestore.service.BaseMongoService;
import com.yunsheng.filestore.service.MongoFileService;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("mongoFileService")
public class MongoFileServiceImpl implements MongoFileService {

    private static final Map<String, GridFS> fsMap = new ConcurrentHashMap<>();
    private static final String UPLOAD_DATE = "uploadDate";
    private static final String CONTENT_TYPE = "contentType";
    private static final String FILE_UUID = "fileUUID";
    private static final String FILE_ID = "_id";
    private static final long GRIDFS_CHUNK_SIZE = 16 * 1024 * 1000;//设置块大小为16M。默认是256K

    @Autowired
    private BaseMongoService baseMongoService;

    @Override
    public FileResult saveFile(FileRequest request) {
        FileResult result = new FileResult();
        result.setSuccess(false);
        try {
            if (request == null) {
                result.setMsg("param cannot null");
                return result;
            }
            if (request.getBytes() == null || request.getBytes().length < 1) {
                result.setMsg("file cannot empty");
                return result;
            }
            if (StringUtils.isBlank(request.getAppName())) {
                result.setMsg("appName cannot empty");
                return result;
            }


            GridFS gridFS = getGridFS(request.getAppName());
            if (gridFS == null) {
                result.setMsg("host or port or appName invalid");
                return result;
            }
            GridFSInputFile mongofile = gridFS.createFile(request.getBytes());
            if (StringUtils.isNotBlank(request.getFileName())) {
                mongofile.setFilename(request.getFileName());
            }
            String tmp = mongofile.getFilename();
            String uuid = UUID.randomUUID().toString();
            mongofile.put(UPLOAD_DATE, new Date());
            if (StringUtils.isNotBlank(tmp)) {
                mongofile.put(CONTENT_TYPE, tmp.substring(tmp.lastIndexOf(".") + 1));
            }
            mongofile.put(FILE_UUID, uuid);
            mongofile.put(FILE_ID, uuid);
            long size = request.getBytes().length;
            if (size < GRIDFS_CHUNK_SIZE) {
                mongofile.setChunkSize(size);
                mongofile.save();
            } else {
                mongofile.setChunkSize(GRIDFS_CHUNK_SIZE);
                mongofile.save();
            }

            result.setFileUUID(uuid);
            result.setSuccess(true);
            result.setMsg("success");
        } catch (Exception e) {
            log.error("saveFile error,appName=" + request.getAppName(), e);
            result.setMsg(e != null ? e.toString() : "");
        }
        return result;
    }

    @Override
    public FileResult findFileByUUID(FileRequest request) {
        FileResult result = new FileResult();
        result.setSuccess(false);
        try {
            if (!commonCheck(request.getAppName(), request.getFileId(), result)) {
                return result;
            }

            GridFS gridFS = getGridFS(request.getAppName());
            if (gridFS == null) {
                result.setMsg("host or port or appName invalid");
                return result;
            }
            DBObject query = new BasicDBObject();
            query.put(FILE_UUID, request.getFileId());
            List<GridFSDBFile> fileList = gridFS.find(query);
            if (fileList != null && !fileList.isEmpty()) {
                GridFSDBFile gFile = fileList.get(0);
                ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
                gFile.writeTo(byteStream);
                result.setFileBytes(byteStream.toByteArray());
                result.setFileName(gFile.getFilename());
                result.setFileContent(gFile.getContentType());
            }
            result.setSuccess(true);
            result.setMsg("success");
        } catch (Exception e) {
            log.error("findFileByUUID error,appName=" + request.getAppName() + ",fileId=" + request.getFileId(), e);
            result.setMsg(e != null ? e.toString() : "");
        }
        return result;
    }

    /**
     * 获取gridfs对象
     */
    private GridFS getGridFS(String appName) {
//    public GridFS getGridFS(List<ServerAddress> addressList, String appName) {
//        if (addressList == null || addressList.isEmpty() || StringUtils.isBlank(appName)) {
//            return null;
//        }

        String gridFSKey = appName;
        if (fsMap.get(gridFSKey) != null) {
            return fsMap.get(gridFSKey);
        }

        synchronized (MongoFileServiceImpl.class) {
            if (fsMap.get(gridFSKey) != null) {
                return fsMap.get(gridFSKey);
            }
            DB appDB = baseMongoService.getDB(appName);
            if (appDB == null) {
                return null;
            }
            GridFS gridFS = new GridFS(appDB);
            fsMap.put(gridFSKey, gridFS);
        }
        return fsMap.get(gridFSKey);
    }

    private boolean commonCheck(String appName, String fileUUID, FileResult result) {
        if (StringUtils.isBlank(appName)) {
            result.setMsg("appName cannot empty");
            return false;
        }
        if (StringUtils.isBlank(fileUUID)) {
            result.setMsg("fileUUID cannot empty");
            return false;
        }
        return true;
    }
}
