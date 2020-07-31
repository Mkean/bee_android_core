package com.bee.core.delegate

import android.content.Context

/**
 *@Description: Application 代理类，组件继承实现，用于框架分发 Application 生命周期
 *
 */
interface IApplicationDelegate {

    fun onCreate(context: Context)
}