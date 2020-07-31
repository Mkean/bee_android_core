package com.bee.home.fragment

import com.bee.android.common.component.HOME
import com.bee.android.common.component.LAUNCHER
import com.bee.core.bean.ComponentResBean
import com.bee.core.delegate.IActivityDelegate
import com.bee.core.delegate.IApplicationDelegate
import com.bee.core.delegate.IComponentDescription
import com.bee.home.fragment.constant.HOME_ENTRANCE_FRAGMENT

/**
 *@Description:
 *
 */
class HomeComponent : IComponentDescription {

    companion object {
        private const val SELECT_ASSET_NAME = "lottie/home/home_shouye_un_se/shouye2.json"

        private const val SELECT_IMAGE_ASSETS_FOLDER = "lottie/home/home_shouye_un_se/images/"

        private const val UN_SELECT_ASSET_NAME = "lottie/home/home_shouye_se_un/shouye1.json"

        private const val UN_SELECT_IMAGE_ASSETS_FOLDER = "lottie/home/home_shouye_se_un/images/"
    }

    private var componentResBean :ComponentResBean?=null;

    override fun getEntranceRouteUrl(): String = HOME_ENTRANCE_FRAGMENT

    override fun getGroup(): String = LAUNCHER

    override fun getName(): String ="首页"

    override fun getComponentTag(): String = HOME

    override fun getApplicationDelegate(): IApplicationDelegate =HomeApplicationDelegate()

    override fun getActivityDelegate(): IActivityDelegate =HomeActivityDelegate()

    override fun getComponentRes(): ComponentResBean {
        if(componentResBean == null){
            componentResBean  = ComponentResBean(SELECT_ASSET_NAME,
            SELECT_IMAGE_ASSETS_FOLDER, UN_SELECT_ASSET_NAME, UN_SELECT_IMAGE_ASSETS_FOLDER);
        }
        return componentResBean!!
    }
}
