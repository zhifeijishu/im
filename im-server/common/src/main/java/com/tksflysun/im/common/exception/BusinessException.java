package com.tksflysun.im.common.exception;

/**
 * 
 * @author
 * @date 2019/03/27 自定义业务异常类
 */
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private String message;
    private String code;

    public BusinessException(String message, String code) {
        super();
        this.message = message;
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public BusinessException() {}

    public BusinessException(String message) {
        this.message = message;
    }

}
