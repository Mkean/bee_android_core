package com.bee.home.fragment.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 *@Description:首页顶部轮播图对象
 *
 */
data class HomeBannerBean(

    @SerializedName("title")
    val title: String, // 广告名称

    @SerializedName("id")
    val id: String, // 广告ID

    @SerializedName("sort")
    val sort: String, // 排序

    @SerializedName("subject_id")
    val subject_id: String, // 学科类型

    @SerializedName("img_type")
    val img_type: String, // 必须 1 图片  2 视频

    @SerializedName("img_url")
    val img_url: String, // 必须  图片地址

    @SerializedName("web_url")
    val web_url: String //  必须  跳转地址


) : Serializable {

}