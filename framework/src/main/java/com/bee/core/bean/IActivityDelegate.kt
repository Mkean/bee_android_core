package com.bee.core.bean

import android.content.Context

/**
 *@Description: Activity 代理类，组件继承实现，用于框架分发 Activity 生命周期
 */
interface IActivityDelegate {

    fun onCreate(context: Context)
}