package com.bee.android.common.network.response;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class ApiResult<T> implements Serializable {

    public static final int STATUS_OK = 200;
    public static final int STATUS_ERROR = 300;
    public static final int STATUS_TIMEOUT = 301;

    private int status;
    private String message;
    private T data;
    private List<Map<String, String>> errors;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public List<Map<String, String>> getErrors() {
        return errors;
    }

    public void setErrors(List<Map<String, String>> errors) {
        this.errors = errors;
    }
}
