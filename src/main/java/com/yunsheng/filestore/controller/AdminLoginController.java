package com.yunsheng.filestore.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;


@Controller
@RequestMapping("/")
@Slf4j
public class AdminLoginController {

    @RequestMapping(value = "login")
    public ModelAndView login() {
        log.info("login===");
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        modelAndView.addObject("name", "IT技术圈");
        return modelAndView;
    }


}
