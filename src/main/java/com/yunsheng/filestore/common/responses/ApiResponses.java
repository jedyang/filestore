package com.yunsheng.filestore.common.responses;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

/**
 * 接口返回(多态)
 *
 * @author yunsheng
 */
public class ApiResponses<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 不需要返回结果
     */
    public static ApiResponses<Void> empty() {
        return SuccessResponses.<Void>builder().status(HttpStatus.OK.value()).build();

    }

    /**
     * 成功返回
     *
     * @param object
     */
    public static <T> ApiResponses<T> success(T object) {
        return SuccessResponses.<T>builder().status(HttpStatus.OK.value()).result(object).build();

    }

    /**
     * 失败返回
     *
     * @param errorCode
     * @param exception
     */
    public static <T> ApiResponses<T> failure(ErrorCodeEnum errorCode, Exception exception) {
//        return FailedResponse.builder().status(errorCode.httpCode()).msg(errorCode.msg()).build();
        return FailedResponse.exceptionMsg(FailedResponse.builder().msg(errorCode.msg()), exception)
                .error(String.valueOf(errorCode.httpCode()))
                .show(errorCode.show())
                .time(LocalDateTime.now())
                .status(errorCode.httpCode())
                .build();
    }

}
