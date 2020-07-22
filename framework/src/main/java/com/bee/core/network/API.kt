package com.bee.core.network

object API {
    /**
     * 根据初始化时传入的baseUrl构成的Retrofit来createService
     *
     *@param service
     *@return
     */
    fun <T> service(service: Class<T>): T {
        return NetWorkManager.getInstance().createService(service)
    }

    /**
     * 根据当前传入的baseUrl参数构成的Retrofit来createService
     *
     *@param baseUrl
     *@param service
     *@return
     */
    fun <T> service(baseUrl: String, service: Class<T>): T {
        return NetWorkManager.getInstance().createService(baseUrl, service)
    }
}