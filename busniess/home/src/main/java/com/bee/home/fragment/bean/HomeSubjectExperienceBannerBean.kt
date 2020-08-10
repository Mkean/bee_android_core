package com.bee.home.fragment.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.lang.Exception

/**
 *@Description: 首页---课程卡片---底图对象
 *
 */

data class HomeSubjectExperienceBannerBean(

    @SerializedName("banner_type")
    val banner_type: String, // 头图类型  0视频  1图片  2 GIF

    @SerializedName("bg_img_url")
    val bg_img_url: String, // 图片地址：如果是视频类型则是 视频封面地址

    @SerializedName("bg_img_url_video")
    val bg_img_url_video: String // 视频地址

) : Serializable {

    companion object {
        const val banner_type_video = 0
        const val banner_type_image = 1
    }

    fun getBannerType(): Int {
        var type = 1

        try {
            type = banner_type.toInt()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return type
    }

}