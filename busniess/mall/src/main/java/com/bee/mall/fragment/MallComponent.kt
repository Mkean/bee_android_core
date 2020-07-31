package com.bee.mall.fragment

import com.bee.android.common.component.LAUNCHER
import com.bee.android.common.component.MALL
import com.bee.core.bean.ComponentResBean
import com.bee.core.delegate.IActivityDelegate
import com.bee.core.delegate.IApplicationDelegate
import com.bee.core.delegate.IComponentDescription
import com.bee.mall.fragment.constant.MALL_ENTRANCE_FRAGMENT

/**
 *@Description:
 *
 */
class MallComponent:IComponentDescription {

    companion object{
        private const val SELECT_ASSET_NAME ="lottie/home/home_shop_un_se/shop2.json"
        private const val SELECT_IMAGE_ASSETS_FOLDER ="lottie/home/home_shop_un_se/images/"
        private const val UN_SELECT_ASSET_NAME ="lottie/home/home_shop_se_un/shop1.json"
        private const val UN_SELECT_IMAGE_ASSETS_FOLDER ="lottie/home/home_shop_se_un/images/"
    }

    private var componentResBean:ComponentResBean?=null

    override fun getEntranceRouteUrl(): String = MALL_ENTRANCE_FRAGMENT

    override fun getGroup(): String = LAUNCHER

    override fun getName(): String ="商城"

    override fun getComponentTag(): String  = MALL

    override fun getApplicationDelegate(): IApplicationDelegate =MallApplicationDelegate()

    override fun getActivityDelegate(): IActivityDelegate =MallActivityDelegate()

    override fun getComponentRes(): ComponentResBean {
        if(componentResBean == null){
            componentResBean = ComponentResBean(
                    SELECT_ASSET_NAME, SELECT_IMAGE_ASSETS_FOLDER,
                    UN_SELECT_ASSET_NAME, UN_SELECT_IMAGE_ASSETS_FOLDER)
        }
        return componentResBean!!
    }
}