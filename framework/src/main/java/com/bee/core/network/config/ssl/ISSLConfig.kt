package com.bee.core.network.config.ssl

/**
 *@Description:
 *
 */
interface ISSLConfig {
    // 获取https认证状态
    fun getSSLState(): SSLState

    // 客户端证书在asset文件名称
    fun getClientCer(): String

    // 客户端证书密码 推荐配置到gradle中，不推荐明文写在项目中
    fun getClientPassword():String

    // 服务端证书文件名称
    fun getServerCer(): String
}