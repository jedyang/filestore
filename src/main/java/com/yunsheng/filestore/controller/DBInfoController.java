package com.yunsheng.filestore.controller;

import com.yunsheng.filestore.common.responses.ApiResponses;
import com.yunsheng.filestore.common.responses.ReturnApi;
import com.yunsheng.filestore.entity.AppDBInfo;
import com.yunsheng.filestore.entity.ApplyInfo;
import com.yunsheng.filestore.service.ApplyService;
import com.yunsheng.filestore.service.MongoDBService;
import com.yunsheng.filestore.service.PermissionService;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;


@Controller
@Slf4j
@Api("库信息相关的api")
@RequestMapping("/dbinfo")
public class DBInfoController {

    @Autowired
    private MongoDBService mongoDBService;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private ApplyService applyService;

    @RequestMapping(value = "/filestore/{page}", method = RequestMethod.GET)
    public ModelAndView filestore(@PathVariable("page") String page) {
        log.info("6show:" + page);

        ModelAndView view = new ModelAndView();

        view.setViewName("/pages/filestore/" + page);

        return view;
    }


    /**
     * 返回库的信息
     */
    @GetMapping("/queryInfo")
    @ResponseBody
    public ApiResponses<List<AppDBInfo>> queryInfo(@RequestParam(name = "page") Integer page, @RequestParam(name = "limit") Integer limit) {

        List<AppDBInfo> allAppDBInfo;
        long count = 0;
        try {

            // 查看登录的用户
            Subject subject = SecurityUtils.getSubject();
            // 如果是管理员权限的，不用传userName，查询全部
            Set<String> permissions = null;
            boolean admin = subject.hasRole("admin");
            if (!admin) {
                String userName = (String) subject.getPrincipal();
                // TODO 拿权限列表，权限列表就是有权限的库的列表
                permissions = permissionService.getPermissions(userName);
            }

            allAppDBInfo = mongoDBService.getAllAppDBInfo(page, limit, permissions);
            count = mongoDBService.countAllAppDB();

            for (AppDBInfo appDBInfo : allAppDBInfo) {

                AppDBInfo detail = null;
                try {
                    detail = mongoDBService.getDbDetail(appDBInfo.getDbName());
                } catch (Exception e) {
                    e.printStackTrace();
                    continue;
                }
                appDBInfo.setStorageSize(detail.getStorageSize());
                appDBInfo.setDataSize(detail.getDataSize());
            }

        } catch (Exception e) {
            log.error(e.getMessage());
            ApiResponses<List<AppDBInfo>> responses = new ApiResponses<>();
            responses.setCode(1);
            responses.setMsg("fail");
            return responses;

        }
        ApiResponses<List<AppDBInfo>> responses = new ApiResponses<>();
        responses.setCode(0);
        responses.setMsg("success");
        responses.setData(allAppDBInfo);
        responses.setCount(count);
        return responses;
    }

    // 数据库详情页
    @RequestMapping(value = "/dbDetail/{dbName}", method = RequestMethod.GET)
    public ModelAndView dbDetail(@PathVariable("dbName") String dbName) {
        log.info("dbDetail page:" + dbName);

        ModelAndView view = new ModelAndView();

        view.setViewName("/pages/filestore/dbDetail");
        AppDBInfo dbDetail = mongoDBService.getDbDetail(dbName);

        view.addObject("dbDetail", dbDetail);
        return view;
    }

    /**
     * 返回库的集合信息
     */
    @ApiOperation(value = "查看集合信息", notes = "全部显示所有集合")
    @RequestMapping(value = "/collectionInfo", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> queryInfo() {

        Map<String, Object> data = new HashMap<>();

        try {
            List<AppDBInfo> allAppDBInfo = mongoDBService.getAllAppDBInfo(0, 100, null);

            for (AppDBInfo appDBInfo : allAppDBInfo) {
                Map<String, String> collectionInfo = mongoDBService.getCollectionInfo(appDBInfo);
                data.put(appDBInfo.getDbName(), collectionInfo);
            }

        } catch (Exception e) {
            log.error("collectionInfo", e);
            return ReturnApi.error("fail");

        }

        return ReturnApi.success("success", data);
    }


    /**
     * 返回库数据的详细信息共画图使用
     */
    @ApiOperation(value = "图表所需信息", notes = "返回数据供ecchart画图使用")
    @RequestMapping(value = "/loadChartData/{dbName}", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> loadChartData(@PathVariable(name = "dbName") String dbName) {
        Map<String, Object> data = new HashMap<>();

        Map<String, List<String>> chartData = mongoDBService.getChartData(dbName);
        List<String> xAxisData = chartData.get("xAxis");
        data.put("xAxis", xAxisData);

        List<String> seriesData = chartData.get("series");
        data.put("series", seriesData);

        return ReturnApi.success("success", data);
    }


    /**
     * 数据库申请
     */
    @ApiOperation(value = "业务存储服务申请", notes = "业务存储服务申请")
    @RequestMapping(value = "/apply", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> apply(@RequestBody ApplyInfo applyInfo) {
        Map<String, Object> data = new HashMap<>();

        log.info(applyInfo.toString());

        int i = applyService.insertApply(applyInfo);
        if (i == 0) {
            return ReturnApi.error("申请失败，请联系管理员");
        }

        return ReturnApi.success("申请成功，请等待审批", data);
    }

    /**
     * 数据库申请
     */
    @ApiOperation(value = "审核业务存储服务申请", notes = "审核业务存储服务申请")
    @RequestMapping(value = "/updataApplyInfo", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> updataApplyInfo(@RequestBody ApplyInfo applyInfo) {

        log.info(applyInfo.toString());

        long i = applyService.updateApply(applyInfo);
        if (i == 0) {
            return ReturnApi.error("审核失败");
        }

        return ReturnApi.success("审核完成", null);
    }


    /**
     * 数据库申请查询
     */
    @ApiOperation(value = "查询业务存储服务申请", notes = "查询业务存储服务申请")
    @RequestMapping(value = "/queryApplyInfo", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponses<List<ApplyInfo>> queryApplyInfo() {
        Map<String, Object> data = new HashMap<>();
        // 查看登录的用户
        Subject subject = SecurityUtils.getSubject();
        String applyName = (String) subject.getPrincipal();
        if (subject.hasRole("admin")) {
            // 管理员全部查询
            applyName = "";
        }

        List<ApplyInfo> applyInfos = applyService.queryApplyInfo(applyName);

        ApiResponses<List<ApplyInfo>> responses = new ApiResponses<>();
        responses.setCode(0);
        responses.setMsg("success");
        responses.setData(applyInfos);
        responses.setCount(applyInfos.size());
        return responses;
    }
}
