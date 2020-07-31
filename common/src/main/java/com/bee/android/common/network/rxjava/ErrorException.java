package com.bee.android.common.network.rxjava;

public class ErrorException extends Exception {
    private String code;
    private String message;

    public ErrorException(String code, String message) {
        this.code = code;
        this.message = message;
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

    @Override
    public String toString() {
        return "ErrorException{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
