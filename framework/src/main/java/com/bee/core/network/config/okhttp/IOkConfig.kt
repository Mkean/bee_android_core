package com.bee.core.network.config.okhttp

interface IOkConfig {
    // 超时时间
    fun getConnectTimeOut(): Int

    // 写入时间
    fun getWriteTimeOut(): Int

    // 读取时间
    fun getReadTimeOut(): Int

    // 缓存文件夹路径
    fun getCacheDirPath(): String

    // 缓存文件夹大小
    fun getCacheSize(): Int

    //最大并发线程数
    fun getMaxRequests(): Int
}