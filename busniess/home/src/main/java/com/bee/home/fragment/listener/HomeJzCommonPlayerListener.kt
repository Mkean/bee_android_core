package com.bee.home.fragment.listener

/**
 *@Description:首页--课程卡片--播放器，播放状态监听
 *
 */
interface HomeJzCommonPlayerListener {

    fun onPrepared()

    fun onStatePreparing()

    fun onStatePlaying()

    fun onStateAutoComplete()

    fun onStateError()

    fun onReset()
}