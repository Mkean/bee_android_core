package com.bee.home.fragment.bean

import com.bee.core.utils.TimeUtil
import com.bee.home.fragment.bean.HomeSubjectExperienceBannerBean.Companion.banner_type_image
import com.bee.home.fragment.bean.HomeSubjectExperienceBannerBean.Companion.banner_type_video
import com.bee.home.fragment.listener.HomeSubjectBeanInterface
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.lang.Exception

/**
 *@Description: 首页---课程卡片---系统课bean对象
 *
 */

data class HomeSubjectSystemBean(

    @SerializedName("course_type")
    val course_type: String,

    @SerializedName("course_type_name")
    val course_type_name: String,

    @SerializedName("discount_name")
    val discount_name: String, // 优惠文案

    @SerializedName("open_course_time")
    val open_course_time: String, // 上课日期

    @SerializedName("sale_point_name")
    val sale_point_name: String, // 上课文案

    @SerializedName("period")
    val period: String, // 课程期数

    @SerializedName("card_promotion_price")
    val card_promotion_price: String, // 价格--优惠前

    @SerializedName("card_price")
    val card_price: String, // 价格---优惠后

    @SerializedName("unit")
    val unit: String, // 价格---单价

    @SerializedName("banner_type")
    val banner_type: String, // 卡片底图类型

    @SerializedName("banner_list")
    val banner_list: List<HomeSubjectExperienceBannerBean>?, // 卡片头图列表

    @SerializedName("web_url")
    val web_url: String, // web 地址

    @SerializedName("button_name")
    val button_name: String // 按钮名称

) : Serializable, HomeSubjectBeanInterface {

    override fun getTypeName(): String = course_type_name

    override fun getDiscountName(): String = discount_name

    override fun getOpenCourseTime(): String {
        try {
            var time = open_course_time.toLong()
            time *= 1000
            return TimeUtil.longToString(time, TimeUtil.FORMAT_MM_DD)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return open_course_time
    }

    fun getBannerType(): Int {
        try {
            return banner_type.toInt()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return banner_type_image
    }

    override fun getSalePointName(): String = sale_point_name

    override fun getUnits(): String = unit

    override fun getCardPrice(): String = card_price

    override fun getCardPromotionPrice(): String = card_promotion_price

    override fun getBannerVideoBean(): HomeSubjectExperienceBannerBean? {
        if (banner_list != null && banner_list.isNotEmpty()) {
            for (bannerBean in banner_list) {
                if (bannerBean != null && bannerBean.getBannerType() == banner_type_video) {
                    return bannerBean
                }
            }
        }
        return null
    }

    override fun getBannerImgList(): List<HomeSubjectExperienceBannerBean>? {
        if (banner_list != null && banner_list.isNotEmpty()) {
            val imgList = ArrayList<HomeSubjectExperienceBannerBean>();
            for (bannerBean in banner_list) {
                if (bannerBean != null && bannerBean.getBannerType() == banner_type_image) {
                    imgList.add(bannerBean)
                }
            }
            return imgList
        }
        return null
    }

}