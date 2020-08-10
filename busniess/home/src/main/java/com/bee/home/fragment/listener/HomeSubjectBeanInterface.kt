package com.bee.home.fragment.listener

import com.bee.home.fragment.bean.HomeSubjectExperienceBannerBean

/**
 *@Description:首页---课程卡片---顶部展现信息协议接口
 *
 */
interface HomeSubjectBeanInterface {

    fun getTypeName():String

    fun getDiscountName():String

    fun getOpenCourseTime():String

    fun getSalePointName():String

    fun getUnits():String

    fun getCardPrice():String

    fun getCardPromotionPrice():String

    fun getBannerVideoBean(): HomeSubjectExperienceBannerBean?

    fun getBannerImgList():List<HomeSubjectExperienceBannerBean>?
}