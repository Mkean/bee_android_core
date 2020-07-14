package com.bee.launch.network.rxjava;

import com.bee.android.common.network.rxjava.Optional;

public class ResultData<T> extends ApiResult<T> {
    private T data;
    private int statusCode;
    private String message;
    private int status;

    @Override
    public T getData() {
        return data;
    }

    @Override
    public void setData(T data) {
        this.data = data;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public void setStatus(int status) {
        this.status = status;
    }

    public Optional<T> transform() {
        return new Optional<>(data);
    }
}
