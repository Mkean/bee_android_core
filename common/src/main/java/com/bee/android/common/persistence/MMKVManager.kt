package com.bee.android.common.persistence

import android.content.Context
import com.tencent.mmkv.MMKV

/**
 *MMKV 初始化管理等相关操作
 */
object MMKVManager {

    /**
     *默认把文件存放在 $(FileDir)/mmkv/目录
     */
    fun initialize(context: Context): String? = MMKV.initialize(context)

    /**
     *官方推荐将 MMKV 文件存储在你的 APP 的私有路径内部
     */
    fun initialize(rootDir: String) {
        MMKV.initialize(rootDir)
    }

    fun defaultMMKV(): MMKV = MMKV.defaultMMKV()

    /**
     *如果不同业务需要区别存储，可以单独创建自己的实例
     */
    fun mmkvWithId(mmapID: String): MMKV = MMKV.mmkvWithID(mmapID)

    /**
     *MMKV 默认把文件存放在$(FileDir)/mmkv/目录
     *该方法支持自定义某个文件目录，独立于默认目录之外
     *
     * @param relativePath 文件目录
     */
    fun mmkvWithId_Path(mmapID: String, relativePath: String): MMKV? = MMKV.mmkvWithID(mmapID, relativePath)

    /**
     *如果业务需要多进程访问，采用这种方式创建实例
     */
    fun mmkvWithID_MULIT(mmapID: String): MMKV = MMKV.mmkvWithID(mmapID, MMKV.MULTI_PROCESS_MODE)

    /**
     *线上环境时候需要输出日志
     */
    fun registerHandler() {
        MMKV.registerHandler(MMKVLog())
    }

}