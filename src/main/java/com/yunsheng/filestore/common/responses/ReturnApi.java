package com.yunsheng.filestore.common.responses;

import java.util.HashMap;
import java.util.Map;

/**
 * 统一数据返回
 */
public class ReturnApi {

    /**
     * 成功
     * @param msg
     * @param result
     * @return
     */
    public static Map<String, Object> success(String msg, Object result) {
        Map<String, Object> map = new HashMap();
        map.put("status", 1);
        map.put("msg", msg);
        map.put("result", result);
        return map;
    }

    /**
     * 失败
     * @param msg
     * @return
     */
    public static Map<String, Object> error(String msg) {
        Map<String, Object> map = new HashMap();
        map.put("status", 0);
        map.put("msg", msg);
        return map;
    }

    /**
     * 存在异常
     * @param msg
     * @return
     */
    public static Map<String, Object> hasException(String msg) {
        Map<String, Object> map = new HashMap();
        map.put("status", 2);
        map.put("msg", msg);
        return map;
    }
}
