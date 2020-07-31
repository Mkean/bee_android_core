package com.bee.android.common.network;

import com.bee.core.network.rxjava.Optional;

import java.io.Serializable;

/**
 * @Description:
 */
public class BaseResp<T> implements Serializable {

    public String code;
    public String msg;

    public T data;

    /**
     * 服务器时间
     */
    public String current_timestamp;

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }

    public String getCurrent_timestamp() {
        return current_timestamp;
    }

    public Optional<T> transform(){return new Optional<>(data);}
}
