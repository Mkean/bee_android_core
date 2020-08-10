package com.bee.home.fragment.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 *@Description:首页---课程卡片---头像轮播 bean 对象
 *
 */

data class HomeSubjectCardSaleUserBean(

    @SerializedName("show_title")
    val show_title: String, //  展示的文案描述

    @SerializedName("header_url")
    val header_url: String, // 头像地址

    @SerializedName("city")
    val city: String, // 城市

    @SerializedName("name")
    val name: String // 姓名

) : Serializable {

}
