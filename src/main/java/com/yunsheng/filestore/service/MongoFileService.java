package com.yunsheng.filestore.service;

import com.yunsheng.filestore.common.responses.FileRequest;
import com.yunsheng.filestore.common.responses.FileResult;

public interface MongoFileService {
    /**
     * 保存文件，需先将file转换成byte[]
     * 保存文件需设置FileRequest里的bytes、appName、fileName属性
     * 返回结果根据FileResult的success属性判断
     * 如果为true,则会返回对应该文件的唯一id(fileUUID属性)
     * @param request
     * @return
     */
     FileResult saveFile(FileRequest request);

    /**
     * 查询文件需设置FileRequest里的fileId和appName属性
     * 查询结果根据FileResult的success属性判断
     * 如果为true,则会返回文件的byte[](bytes属性)和文件名(fileName属性)
     * @param request
     * @return
     */
     FileResult findFileByUUID(FileRequest request);
}
