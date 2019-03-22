package com.yunsheng.filestore.controller;

import com.yunsheng.filestore.common.responses.ReturnApi;
import com.yunsheng.filestore.entity.User;
import com.yunsheng.filestore.service.UserService;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;


@Controller
@Slf4j
public class AdminLoginController {

    @Autowired
    private UserService userService;

    @Value("${me.env}")
    private String env;

    @RequestMapping(value = "/login")
    public ModelAndView login() {
        ModelAndView view = new ModelAndView();

        view.addObject("env", env);

        view.setViewName("login");

        return view;
    }

    /**
     * 登录校验
     */
    @RequestMapping(value = "/api/loginCheck", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> loginCheck(@RequestBody Map<String, String> params) {


        String username = params.get("username");
        String password = params.get("password");

        //登录后存放进shiro token
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        Subject subject = SecurityUtils.getSubject();

        try {
            subject.login(token);
            //根据权限，指定返回数据
            boolean admin = subject.hasRole("admin");
            if (admin){
                // 演示可以根据权限做不同的事情
                log.info("admin login");
            }

        } catch (AuthenticationException e) {
            e.printStackTrace();
            return ReturnApi.error("登录失败");
        }

        return ReturnApi.success("登录成功", null);
    }


    /**
     * 用户注册
     */
    @ApiOperation(value = "用户注册", notes = "用户注册")
    @RequestMapping(value = "/api/register", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> register(@RequestBody User params) {

        try {
            int i = userService.insertUser(params);
            if (i == 1) {
                return ReturnApi.success("注册成功", null);
            } else if (i == 11000) {
                return ReturnApi.error("注册失败,用户名重复");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ReturnApi.error("注册失败");
    }

}
