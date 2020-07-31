package com.bee.course.fragment

import com.bee.android.common.component.COURSE
import com.bee.android.common.component.LAUNCHER
import com.bee.core.bean.ComponentResBean
import com.bee.core.delegate.IActivityDelegate
import com.bee.core.delegate.IApplicationDelegate
import com.bee.core.delegate.IComponentDescription
import com.bee.course.fragment.constant.COURSE_ENTRANCE_FRAGMENT

/**
 *@Description:
 *
 */
class CourseComponent :IComponentDescription{

    companion object{
        private const val SELECT_ASSET_NAME= "lottie/home/home_source_un_se/kecheng2.json"

        private const val SELECT_IMAGE_ASSETS_FOLDER= "lottie/home/home_source_un_se/images/"

        private const val UN_SELECT_ASSET_NAME= "lottie/home/home_source_se_un/kecheng1.json"

        private const val UN_SELECT_IMAGE_ASSETS_FOLDER= "lottie/home/home_source_se_un/images/"

    }

    private var componentResBean:ComponentResBean?=null

    override fun getEntranceRouteUrl(): String = COURSE_ENTRANCE_FRAGMENT

    override fun getGroup(): String = LAUNCHER

    override fun getName(): String ="课程"

    override fun getComponentTag(): String = COURSE

    override fun getApplicationDelegate(): IApplicationDelegate =CourseApplication()

    override fun getActivityDelegate(): IActivityDelegate =CourseActivity()

    override fun getComponentRes(): ComponentResBean {
        if(componentResBean == null){
            componentResBean = ComponentResBean(
                    SELECT_ASSET_NAME, SELECT_IMAGE_ASSETS_FOLDER,
                    UN_SELECT_ASSET_NAME, UN_SELECT_IMAGE_ASSETS_FOLDER)
        }
        return componentResBean!!
    }
}