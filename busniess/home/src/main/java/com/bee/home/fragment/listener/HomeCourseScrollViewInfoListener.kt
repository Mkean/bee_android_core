package com.bee.home.fragment.listener

/**
 *@Description:首页--课程卡片---滚动监听---View 信息回调接口
 *
 */
interface HomeCourseScrollViewInfoListener {

    fun getTopY(): Int

    fun getBottomY(): Int

    fun onGetHeight(): Int

    fun isCanPlayVideo(): Boolean

    fun isCanPlayImg(): Boolean

    fun startVideo()

    fun showThumbImg()

    fun startPlayImg()

    fun stopPlayImg()

}