package com.yunsheng.filestore;

import com.yunsheng.filestore.common.responses.FileRequest;
import com.yunsheng.filestore.common.responses.FileResult;
import com.yunsheng.filestore.service.MongoFileService;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {com.yunsheng.filestore.FilestoreApplication.class})
public class FilestoreApplicationTests {

    @Autowired
    private MongoFileService mongoFileService;

    @Test
    public void upload() throws IOException {
        FileRequest request = new FileRequest();
        request.setAppName("hsirrfw");
        request.setFileName("test.png");
        byte[] fileBytes = FileUtils.readFileToByteArray(new File("E:\\图标\\云朵.png"));
        request.setBytes(fileBytes);

        FileResult fileResult = mongoFileService.saveFile(request);

        System.out.println(fileResult);
    }

    // fileUUID=4e2e6352-df5c-421f-8587-9496552669da

}

