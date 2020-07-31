package com.bee.expand.fragment

import com.bee.android.common.component.EXPAND
import com.bee.android.common.component.LAUNCHER
import com.bee.core.bean.ComponentResBean
import com.bee.core.delegate.IActivityDelegate
import com.bee.core.delegate.IApplicationDelegate
import com.bee.core.delegate.IComponentDescription
import com.bee.expand.fragment.constant.EXPAND_ENTRANCE_FRAGMENT

/**
 *@Description:
 *
 */
class ExpandComponent:IComponentDescription {

    companion object{
        private const val SELECT_ASSET_NAME = "lottie/home/home_expand_un_se/tuozhan2.json"
        private const val SELECT_IMAGE_ASSETS_FOLDER = "lottie/home/home_expand_un_se/images/"
        private const val UN_SELECT_ASSET_NAME = "lottie/home/home_expand_se_un/tuozhan1.json"
        private const val UN_SELECT_IMAGE_ASSETS_FOLDER = "lottie/home/home_expand_se_un/images/"
    }

    private var componentResBean:ComponentResBean?= null

    override fun getEntranceRouteUrl(): String = EXPAND_ENTRANCE_FRAGMENT

    override fun getGroup(): String = LAUNCHER

    override fun getName(): String ="扩展"

    override fun getComponentTag(): String = EXPAND

    override fun getApplicationDelegate(): IApplicationDelegate =ExpandApplication()

    override fun getActivityDelegate(): IActivityDelegate = ExpandActivity()

    override fun getComponentRes(): ComponentResBean {
        if(componentResBean == null){
            componentResBean = ComponentResBean(
                    SELECT_ASSET_NAME, SELECT_IMAGE_ASSETS_FOLDER,
                    UN_SELECT_ASSET_NAME, UN_SELECT_IMAGE_ASSETS_FOLDER)
        }
        return componentResBean!!
    }
}