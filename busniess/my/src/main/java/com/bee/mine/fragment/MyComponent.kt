package com.bee.mine.fragment

import com.bee.android.common.component.LAUNCHER
import com.bee.android.common.component.MY
import com.bee.core.bean.ComponentResBean
import com.bee.core.delegate.IActivityDelegate
import com.bee.core.delegate.IApplicationDelegate
import com.bee.core.delegate.IComponentDescription
import com.bee.mine.fragment.constant.MY_ENTRANCE_FRAGMENT

/**
 *@Description:
 *
 */

class MyComponent :IComponentDescription{

    companion object{
        private const val SELECT_ASSET_NAME ="lottie/home/home_me_un_se/wode2.json"
        private const val SELECT_IMAGE_ASSETS_FOLDER = "lottie/home/home_me_un_se/images/"
        private const val UN_SELECT_ASSET_NAME = "lottie/home/home_me_se_un/wode1.json"
        private const val UN_SELECT_IMAGE_ASSETS_FOLDER="lottie/home/home_me_se_un/images/"

    }

    private var componentResBean:ComponentResBean?=null

    override fun getEntranceRouteUrl(): String = MY_ENTRANCE_FRAGMENT

    override fun getGroup(): String = LAUNCHER

    override fun getName(): String ="我的"

    override fun getComponentTag(): String = MY

    override fun getApplicationDelegate(): IApplicationDelegate =MyApplicationDelegate()

    override fun getActivityDelegate(): IActivityDelegate =MyActivityDelegate()

    override fun getComponentRes(): ComponentResBean {
        if(componentResBean == null){
            componentResBean = ComponentResBean(
                    SELECT_ASSET_NAME, SELECT_IMAGE_ASSETS_FOLDER,
                    UN_SELECT_ASSET_NAME, UN_SELECT_IMAGE_ASSETS_FOLDER)
        }
        return componentResBean!!
    }

}