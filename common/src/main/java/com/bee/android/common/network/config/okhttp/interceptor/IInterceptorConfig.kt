package com.bee.android.common.network.config.okhttp.interceptor

import com.bee.android.common.network.config.okhttp.OkConfig
import okhttp3.Interceptor

interface IInterceptorConfig {

    /**
     * 添加拦截器
     */
    fun addInterceptor(interceptor: Interceptor?): OkConfig

    /**
     * 添加网络拦截器
     */
    fun addNetworkInterceptor(interceptor: Interceptor?): OkConfig

    /**
     *获取拦截器
     */
    fun getInterceptor(): List<Interceptor>?

    /**
     * 获取网络拦截器
     */
    fun getNetworkInterceptor(): List<Interceptor>?

}