package com.bee.core.network.config.okhttp;

import android.content.Context;


import com.bee.core.network.config.cookies.CookiesManager;
import com.bee.core.network.config.cookies.ICookies;
import com.bee.core.network.config.okhttp.interceptor.IInterceptorConfig;
import com.bee.core.network.config.okhttp.interceptor.InterceptorArray;
import com.bee.core.network.config.ssl.ISSLConfig;
import com.bee.core.network.config.ssl.SSLState;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import okhttp3.Interceptor;

public class OkConfig implements IOkConfig, IInterceptorConfig, ICookies, ISSLConfig {

    private static volatile OkConfig okConfig;

    private OkConfig() {
    }

    public static OkConfig getOkConfig() {
        if (okConfig == null) {
            synchronized (OkConfig.class) {
                if (okConfig == null) {
                    okConfig = new OkConfig();
                }
            }
        }
        return okConfig;
    }


    // 连接超时时长
    public int default_connectTimeOut = 30;
    // 写入超时时长
    public int default_writeTimeOut = 30;
    // 读取超时时长
    public int default_readTimeOut = 30;
    // 缓存路径
    public String default_cacheDir = "/Demo/network";
    // 缓存大小
    public int default_cacheSize = 1024 * 1024 * 10;
    // 默认最大并发线程数
    public int default_maxRequests = 8;


    @Override
    public OkConfig addInterceptor(Interceptor interceptor) {
        if (interceptor != null) {
            InterceptorArray.interceptors.add(interceptor);
        } else {
            throw new IllegalArgumentException("interceptor must be not null");
        }
        return this;
    }

    @NotNull
    @Override
    public OkConfig addNetworkInterceptor(Interceptor interceptor) {
        if (interceptor != null) {
            InterceptorArray.networkInterceptors.add(interceptor);
        } else {
            throw new IllegalArgumentException("networkInterceptor must be not null");
        }
        return this;
    }

    @Override
    public List<Interceptor> getInterceptor() {
        return InterceptorArray.interceptors;
    }

    @Override
    public List<Interceptor> getNetworkInterceptor() {
        return InterceptorArray.networkInterceptors;
    }

    @Override
    public int getConnectTimeOut() {
        return default_connectTimeOut;
    }

    @Override
    public int getWriteTimeOut() {
        return default_writeTimeOut;
    }

    @Override
    public int getReadTimeOut() {
        return default_readTimeOut;
    }

    @NotNull
    @Override
    public String getCacheDirPath() {
        return default_cacheDir;
    }

    @Override
    public int getCacheSize() {
        return default_cacheSize;
    }

    @Override
    public int getMaxRequests() {
        return default_maxRequests;
    }

    public OkConfig setConnectTimeOut(int connectTimeOut) {
        this.default_connectTimeOut = connectTimeOut;
        return this;
    }

    public OkConfig setWriteTimeOut(int default_writeTimeOut) {
        this.default_writeTimeOut = default_writeTimeOut;
        return this;
    }

    public OkConfig setReadTimeOut(int default_readTimeOut) {
        this.default_readTimeOut = default_readTimeOut;
        return this;
    }

    public OkConfig setCacheDir(String default_cacheDir) {
        this.default_cacheDir = default_cacheDir;
        return this;
    }

    public OkConfig setCacheSize(int default_cacheSize) {
        this.default_cacheSize = default_cacheSize;
        return this;
    }

    public OkConfig setMaxRequests(int default_maxRequests) {
        this.default_maxRequests = default_maxRequests;
        return this;
    }

    @Override
    public CookiesManager getCookiesManager(Context context) {
        return new CookiesManager(context);
    }

    @NotNull
    @Override
    public SSLState getSSLState() {
        return SSLState.ALL;
    }

    @NotNull
    @Override
    public String getClientCer() {
        return null;
    }

    @Override
    public String getClientPassword() {
        return null;
    }

    @NotNull
    @Override
    public String getServerCer() {
        return null;
    }
}
