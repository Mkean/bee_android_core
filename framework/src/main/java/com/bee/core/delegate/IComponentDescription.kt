package com.bee.core.delegate

import androidx.annotation.NonNull
import com.bee.core.bean.ComponentResBean
import com.bee.core.bean.IActivityDelegate
import com.bee.core.bean.IApplicationDelegate

/**
 *@Description: 组件入口标准定义
 */
interface IComponentDescription {

    /**
     * 组件唯一标识，需向平台申请，全局唯一
     */
    @NonNull
    fun getComponentTag(): String

    /**
     * 名称
     */
    fun getName(): String

    /**
     * 返回当前组件的入口的路由，Fragment或者Activity
     */
    fun getEntranceRouteUrl(): String

    /**
     * 返回父级组件 Tag
     */
    fun getGroup(): String

    /**
     *  返回组件资源
     */
    fun getComponentRes(): ComponentResBean

    /**
     * 返回 Application 代理类
     */
    fun getApplicationDelegate(): IApplicationDelegate

    /**
     * 返回 Activity 代理类
     */
    fun getActivityDelegate(): IActivityDelegate
}