package com.bee.home.fragment.listener

import com.bee.home.fragment.bean.HomeSubjectExperienceBannerBean
import com.bee.home.fragment.bean.HomeSubjectExperienceBean

/**
 *@Description: 首页课程卡片 ，课程卡片--顶部展现信息协议接口
 *
 */
interface HomeSubjectBeanInterface {

    fun getTypeName(): String

    fun getDiscountName(): String

    fun getOpenCourseTime(): String

    fun getSalePointName(): String

    fun getUnit(): String

    fun getCardPrice(): String

    fun getCardPromotionPrice(): String

    fun getBannerVideoBean(): HomeSubjectExperienceBannerBean?

    fun getBannerImgList(): List<HomeSubjectExperienceBannerBean>?
}