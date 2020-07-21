package com.bee.update.impl;

/**
 * @Description:
 */
public class HttpException extends RuntimeException {

    private int code;
    private String errorMsg;

    public HttpException(int code, String message) {
        this.code = code;
        this.errorMsg = message;
    }


    public int getCode() {
        return code;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    @Override
    public String toString() {
        return "HttpException{" +
                "code=" + code +
                ", errorMsg='" + errorMsg + '\'' +
                '}';
    }
}
