package com.bee.core.delegate;

import androidx.annotation.NonNull;

import com.bee.core.bean.ComponentResBean;

/**
 * @Description: 组件入口标准定义
 */
public interface IComponentDescription {

    /**
     * 组件唯一标识，需向平台申请，全剧唯一
     *
     * @return
     */
    @NonNull
    String getComponentTag();

    /**
     * 名称
     *
     * @return
     */
    String getName();

    /**
     * 返回当前组件的入口的路由，Fragment 或者 Activity
     *
     * @return
     */
    String getEntranceRouteUrl();

    /**
     * 返回夫级组件 TAG
     *
     * @return
     */
    String getGroup();

    /**
     * 获取组件资源
     *
     * @return
     */
    ComponentResBean getComponentRes();

    /**
     * 返回 Application 的代理类
     *
     * @return
     */
    IApplicationDelegate getApplicationDelegate();

    /**
     * 返回 Activity 的代理类
     *
     * @return
     */
    IActivityDelegate getActivityDelegate();
}
