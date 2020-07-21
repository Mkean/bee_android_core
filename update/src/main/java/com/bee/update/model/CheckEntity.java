package com.bee.update.model;

import com.bee.update.UpdateBuilder;
import com.bee.update.UpdateConfig;
import com.bee.update.base.CheckWorker;

import java.util.HashMap;
import java.util.Map;

/**
 * 配置的更新api实体类，此实体类将被{@link CheckWorker}进行使用
 * <p>
 * 配置方式：通过{@link UpdateConfig#setCheckEntity(CheckEntity)}对复杂api数据进行定制; 或者{@link UpdateBuilder#setCheckEntity(CheckEntity)}对简单GET请求的更新api进行定制。
 */
public class CheckEntity {
    private String method = "GET";
    private String url;
    private Map<String, String> params;
    private Map<String, String> headers;

    public String getMethod() {
        return method;
    }

    public CheckEntity setMethod(String method) {
        this.method = method;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public CheckEntity setUrl(String url) {
        this.url = url;
        return this;
    }

    public Map<String, String> getParams() {
        if (params == null) {
            params = new HashMap<>();
        }
        return params;
    }

    public CheckEntity setParams(Map<String, String> params) {
        this.params = params;
        return this;
    }

    public Map<String, String> getHeaders() {
        if (headers == null) {
            headers = new HashMap<>();
        }
        return headers;
    }

    public CheckEntity setHeaders(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }
}
