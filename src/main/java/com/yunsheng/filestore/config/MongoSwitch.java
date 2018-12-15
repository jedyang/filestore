package com.yunsheng.filestore.config;

import com.mongodb.MongoClient;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.mongodb.MongoDbFactory;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.stereotype.Component;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

//@Component
//@Aspect
//@Slf4j
public class MongoSwitch {

    /**
    @Autowired
    private MongoDbFactory mongoDbFactory;
    private Map<String, MongoDbFactory> templateMuliteMap = new HashMap<>();

    @Pointcut("execution(* xxx.xxx.com.dao..*.*(..))")
    public void routeMongoDB() {
    }

    @Around("routeMongoDB()")
    public Object routeMongoDB(ProceedingJoinPoint joinPoint) {
        Object result = null;
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String uri = request.getRequestURL().toString();
        //获取需要访问的项目数据库
        String subject = request.getHeader("subject");
        String name = joinPoint.getSignature().getName();
        Object o = joinPoint.getTarget();
        Field[] fields = o.getClass().getDeclaredFields();
        MultiMongoTemplate mongoTemplate = null;
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                Object fieldObject = field.get(o);
                Class fieldclass = fieldObject.getClass(); //找到Template的变量
                if (fieldclass == MongoTemplate.class || fieldclass == MultiMongoTemplate.class) {
                    //查找项目对应的MongFactory
                    SimpleMongoDbFactory simpleMongoDbFactory = (SimpleMongoDbFactory) templateMuliteMap.get(subject);
                    //实例化
                    if (simpleMongoDbFactory == null) {
                        simpleMongoDbFactory = new SimpleMongoDbFactory(new MongoClient(properties.getHost(), properties.getPort()), properties.getDatabase());
                        templateMuliteMap.put(subject, simpleMongoDbFactory);
                    } //如果第一次，赋值成自定义的MongoTemplate子类
                    if (fieldclass == MongoTemplate.class) {
                        mongoTemplate = new MultiMongoTemplate(simpleMongoDbFactory);
                    } else if (fieldclass == MultiMongoTemplate.class) {
                        mongoTemplate = (MultiMongoTemplate) fieldObject;
                    } //设置MongoFactory
                    mongoTemplate.setMongoDbFactory(simpleMongoDbFactory);
                    //重新赋值
                    field.set(o, mongoTemplate);
                    break;
                }
            }
            try {
                result = joinPoint.proceed();
                //清理ThreadLocal的变量
                mongoTemplate.removeMongoDbFactory();
            } catch (Throwable t) {
                log.error("", t);
            }
        } catch (Exception e) {
            log.error("", e);
        }
        return result;
    }

    **/
}
