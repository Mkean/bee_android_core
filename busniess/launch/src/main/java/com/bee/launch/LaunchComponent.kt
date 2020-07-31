package com.bee.launch

import com.bee.android.common.component.LAUNCHER
import com.bee.core.bean.ComponentResBean
import com.bee.core.delegate.IActivityDelegate
import com.bee.core.delegate.IApplicationDelegate
import com.bee.core.delegate.IComponentDescription

/**
 *@Description:
 *
 */
class LaunchComponent: IComponentDescription {

    override fun getComponentTag(): String = LAUNCHER

    override fun getName(): String =""

    override fun getEntranceRouteUrl(): String? =null

    override fun getGroup(): String? =null

    override fun getComponentRes(): ComponentResBean? =null

    override fun getApplicationDelegate(): IApplicationDelegate =LaunchApplicationDelegate()

    override fun getActivityDelegate(): IActivityDelegate = LaunchActivityDelegate()
}