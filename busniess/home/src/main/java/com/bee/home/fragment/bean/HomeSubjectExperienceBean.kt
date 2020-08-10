package com.bee.home.fragment.bean

import com.bee.core.utils.TimeUtil
import com.bee.home.fragment.bean.HomeSubjectExperienceBannerBean.Companion.banner_type_image
import com.bee.home.fragment.bean.HomeSubjectExperienceBannerBean.Companion.banner_type_video
import com.bee.home.fragment.listener.HomeSubjectBeanInterface
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.lang.Exception

/**
 *@Description: 首页---课程卡片---体验课bean对象
 *
 */

data class HomeSubjectExperienceBean(

    @SerializedName("course_type")
    val course_type: String, // 商品类型  1 体验课  2 系统课

    @SerializedName("course_type_name")
    val course_type_name: String, // 课程类型名称

    @SerializedName("discount_name")
    val discount_name: String, // 优惠标签

    @SerializedName("open_course_time")
    val open_course_time: String, // 开课时间：单位秒

    @SerializedName("sale_point_name")
    val sale_point_name: String, // 卖点说明 -- 适合幼小衔接

    @SerializedName("period")
    val period: String, // 课程期数

    @SerializedName("card_promotion_price")
    val card_promotion_price: String, // 价格---优惠前

    @SerializedName("card_price")
    val card_price: String, // 价格---优惠后

    @SerializedName("unit")
    val unit: String, // 价格---单位


    @SerializedName("banner_type")
    val banner_type: String, // 卡片底图类型  0 视频  1 图片（一张或多张）

    @SerializedName("banner_list")
    val banner_list: List<HomeSubjectExperienceBannerBean>?, // 卡片底图列表


    @SerializedName("sale_state")
    val sale_state: String, // 销售状态类型 1 未开售  2 已开售  3 已售罄

    @SerializedName("sale_state_name")
    val sale_state_name: String, // 销售状态名称  1 未开售  2 已开售  3 已售罄

    @SerializedName("sale_start_time")
    val sale_start_time: String, // 销售--下次开售时间

    @SerializedName("service_time")
    val service_time: String, // 当前服务器时间

    @SerializedName("sale_rest_num")
    val sale_rest_num: String, // 销售---数量---当前剩余

    @SerializedName("show_stock")
    val show_stock: String, // 销售---数量---总数量

    @SerializedName("web_url")
    val web_url: String, // web 地址

    @SerializedName("button_name")
    val button_name: String, // 按钮名称

    @SerializedName("sale_user_list")
    val sale_user_list: List<HomeSubjectCardSaleUserBean>? // 已购买用户数组

) : Serializable, HomeSubjectBeanInterface {

    companion object {
        const val SALE_STATE_END = 3 // 销售状态 --- "已售罄"
        const val SALE_STATE_START = 2 // 销售状态 --- "已开售"
        const val SALE_STATE_NONE = 1 // 销售状态 --- "未开售"
    }

    private val SALE_NATIVE_STATE_END = "3"
    private val SALE_NATIVE_STATE_START = "2"
    private val SALE_NATIVE_STATE_NONE = "1"

    fun getSaleState(): Int {
        return when {
            SALE_NATIVE_STATE_END == sale_state -> {
                SALE_STATE_END
            }
            SALE_NATIVE_STATE_START == sale_state -> {
                SALE_STATE_START
            }
            else -> {
                SALE_STATE_NONE
            }
        }
    }

    /**
     * 获取 下一个销售期时间
     *
     * @return {@code o} 则代表服务器未返回时间
     */
    fun getSaleStartTime(): Long {
        return try {
            (sale_start_time.toLong() * 1000)
        } catch (e: Exception) {
            e.printStackTrace()
            0
        }
    }

    /**
     * 获取服务器时间
     */
    fun getServerTime(): Long {
        return try {
            (service_time.toLong() * 1000)
        } catch (e: Exception) {
            e.printStackTrace()
            0
        }
    }


    fun getBannerType(): Int {
        try {
            return banner_type.toInt()
        } catch (e: Exception) {
            e.printStackTrace()

        }
        return banner_type_image
    }


    override fun getTypeName(): String = course_type_name

    override fun getDiscountName(): String = discount_name

    override fun getOpenCourseTime(): String {
        try {
            var time = open_course_time.toLong();
            time *= 1000
            return TimeUtil.longToString(time, TimeUtil.FORMAT_MM_DD)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return open_course_time
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
            val imgList = ArrayList<HomeSubjectExperienceBannerBean>()
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