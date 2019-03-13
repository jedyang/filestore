package com.yunsheng.filestore;

import com.yunsheng.filestore.common.responses.FileRequest;
import com.yunsheng.filestore.common.responses.FileResult;
import com.yunsheng.filestore.entity.AppDBInfo;
import com.yunsheng.filestore.service.MongoDBService;
import com.yunsheng.filestore.service.MongoFileService;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {com.yunsheng.filestore.FilestoreApplication.class})
@Slf4j
public class FilestoreApplicationTests {

    @Autowired
    private MongoFileService mongoFileService;

    @Autowired
    private MongoDBService mongoDBService;

    //    原shard3：bcs", "cosmocg", "datagov", "ejl", "hcsp", "hmqm", "ifms", "lmp"，
//    原shard4："bcc-rad", "bxdl", "gpms", "his"，
//    原shard6：hrois", "btbrrs", "gxtms", "gxwl", "HRPortal"
    private static String[] transfer2 = {"bcs", "cosmocg", "datagov", "ejl", "hcsp", "hmqm", "ifms", "lmp", "bcc-rad", "bxdl", "gpms", "his", "hrois", "btbrrs", "gxtms", "gxwl", "HRPortal"};
//    private static String[] transfer3 = {"hnmnew", "hmpsdk", "ssc", "dapp","cwgxyg", "dzsp", "gbtp", "hac1169", "hcdc", "hiface", "hopEmail", "hrrk", "hrssc", "isales",
//            "oebs", "ppm", "sci", "scxwxxpt", "showcase", "xwehr", "yuntu", "test", "liip"};

    private static String[] transfer3 = {"paastest"};

    @Test
    public void upload() throws IOException {
        for (String appname : transfer3) {

            FileRequest request = new FileRequest();
            request.setAppName(appname);
            request.setFileName("test.png");
            byte[] fileBytes = FileUtils.readFileToByteArray(new File("E:\\图标\\云朵.png"));
            request.setBytes(fileBytes);

            FileResult fileResult = mongoFileService.saveFile(request);

            System.out.println(appname + ":::::" + fileResult);
        }
    }

    // fileUUID=4e2e6352-df5c-421f-8587-9496552669da

    @Test
    public void testGetCollectionInfo() {
//        AppDBInfo appDBInfo = new AppDBInfo();
//        appDBInfo.setDbName("showcase");
//        Map<String, String> collectionInfo = mongoDBService.getCollectionInfo(appDBInfo);
//        log.info(collectionInfo.toString());

        List<AppDBInfo> allAppDBInfo = mongoDBService.getAllAppDBInfo(0, 100, null);

        List<Map> result = new ArrayList<>();
        for (AppDBInfo appDBInfo : allAppDBInfo) {
            Map<String, String> collectionInfo = mongoDBService.getCollectionInfo(appDBInfo);
            collectionInfo.put("dbName", appDBInfo.getDbName());
            result.add(collectionInfo);
        }

        log.info(result.toString());
    }
}

