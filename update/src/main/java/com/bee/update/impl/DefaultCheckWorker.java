package com.bee.update.impl;

import android.annotation.SuppressLint;

import com.bee.android.common.logger.CommonLogger;
import com.bee.update.UpdateBuilder;
import com.bee.update.UpdateConfig;
import com.bee.update.base.CheckWorker;
import com.bee.update.model.CheckEntity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Set;

/**
 * @Description: 默认提供的检查更新api的网络任务
 * <p>
 * 实现：通过原生api访问网络，并将网络返回的数据直接返回，提供给框架内使用
 * <p>
 * 若需要定制，则可通过{@link UpdateBuilder#setCheckWorker(Class)}或者{@link UpdateConfig#setCheckWorker(Class)}
 */
public class DefaultCheckWorker extends CheckWorker {
    private static final String TAG = "Update";

    @Override
    protected String check(CheckEntity entity) throws Exception {
        HttpURLConnection urlConn = createHttpRequest(entity);

        int responseCode = urlConn.getResponseCode();
        if (responseCode < 200 || responseCode >= 300) {
            urlConn.disconnect();
            throw new HttpException(responseCode, urlConn.getResponseMessage());
        }

        @SuppressLint("NewApi") BufferedReader bis = new BufferedReader(new InputStreamReader(urlConn.getInputStream(), StandardCharsets.UTF_8));

        StringBuilder sb = new StringBuilder();
        String lines;
        while ((lines = bis.readLine()) != null) {
            sb.append(lines);
        }

        urlConn.disconnect();

        return sb.toString();
    }

    @Override
    protected boolean useAsync() {
        return false;
    }

    private HttpURLConnection createHttpRequest(CheckEntity entity) throws IOException {
        if (entity.getMethod().equalsIgnoreCase("GET")) {
            return createGetRequest(entity);
        } else {
            return createPostRequest(entity);
        }
    }

    private void inflateHeaders(Map<String, String> headers, HttpURLConnection connection) {
        Set<String> keys = headers.keySet();
        for (String key : keys) {
            connection.setRequestProperty(key, headers.get(key));
        }
    }

    @SuppressLint("NewApi")
    private HttpURLConnection createPostRequest(CheckEntity entity) throws IOException {
        CommonLogger.d(TAG, "创建POST请求<原生API>");
        URL getUrl = new URL(entity.getUrl());
        HttpURLConnection urlConn = (HttpURLConnection) getUrl.openConnection();
        urlConn.setDoOutput(true);
        urlConn.setConnectTimeout(10000);
        urlConn.setRequestMethod("POST");
        inflateHeaders(entity.getHeaders(), urlConn);
        String params = createParams(entity.getParams());
        urlConn.getOutputStream().write(params.getBytes(StandardCharsets.UTF_8));
        return urlConn;
    }

    private HttpURLConnection createGetRequest(CheckEntity entity) throws IOException {
        CommonLogger.d(TAG, "创建GET请求<原生API>");
        StringBuilder builder = new StringBuilder(entity.getUrl());
        Map<String, String> params = entity.getParams();
        if (params.size() > 0) {
            builder.append("?").append(createParams(params));
        }
        String url = builder.toString();

        URL getUrl = new URL(url);
        HttpURLConnection urlConn = (HttpURLConnection) getUrl.openConnection();
        urlConn.setDoInput(true);
        urlConn.setUseCaches(false);
        urlConn.setConnectTimeout(10000);
        urlConn.setRequestMethod("GET");
        inflateHeaders(entity.getHeaders(), urlConn);
        urlConn.connect();
        return urlConn;
    }

    private String createParams(Map<String, String> params) {
        if (params == null || params.size() == 0) {
            return "";
        }
        StringBuilder paramsBuilder = new StringBuilder();
        for (String key : params.keySet()) {
            paramsBuilder.append(key).append("=").append(params.get(key)).append("&");
        }
        paramsBuilder.deleteCharAt(paramsBuilder.length() - 1);
        return paramsBuilder.toString();
    }
}
