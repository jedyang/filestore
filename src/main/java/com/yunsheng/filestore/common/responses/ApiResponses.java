package com.yunsheng.filestore.common.responses;

import java.io.Serializable;

import lombok.Data;

/**
 * 接口返回
 *
 * @author yunsheng
 */

@Data
public class ApiResponses<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer code;
    private String msg;
    private long count;
    T data;

}
