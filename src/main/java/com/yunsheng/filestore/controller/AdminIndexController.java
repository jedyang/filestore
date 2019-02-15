package com.yunsheng.filestore.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class AdminIndexController {
    @RequestMapping(value = "/index")
    public String index() {
        return "index";
    }

    @RequestMapping(value = "/pages/{page}")
    public String page(@PathVariable("page") String page) {
        log.info("show:" + page);
        return "pages/" + page;
    }

    @RequestMapping(value = "/pages/member/{page}")
    public String member(@PathVariable("page") String page) {
        log.info("show:" + page);
        return "pages/member/" + page;
    }

    @RequestMapping(value = "/pages/admin/{page}")
    public String admin(@PathVariable("page") String page) {
        log.info("show:" + page);
        return "pages/admin/" + page;
    }

    @RequestMapping(value = "/pages/article/{page}")
    public String article(@PathVariable("page") String page) {
        log.info("show:" + page);
        return "pages/article/" + page;
    }

    @RequestMapping(value = "/pages/echarts/{page}")
    public String echarts(@PathVariable("page") String page) {
        log.info("show:" + page);
        return "pages/echarts/" + page;
    }

    @RequestMapping(value = "/pages/order/{page}")
    public String order(@PathVariable("page") String page) {
        log.info("show:" + page);
        return "pages/order/" + page;
    }
}
