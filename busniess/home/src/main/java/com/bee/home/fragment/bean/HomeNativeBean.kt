package com.bee.home.fragment.bean

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 *@Description: 首页所有数据对象，原生页面需要的对象
 *
 */
data class HomeNativeBean(

    @SerializedName("banner_list")
    var banner_list: List<HomeBannerBean>? = null,

    @SerializedName("course")
    var course: List<HomeSubjectBean>? = null


) : Serializable, MultiItemEntity {

    companion object {
        const val TYPE_HOME_DEFAULT = -1; // 默认类型

        const val TYPE_HOME_BANNER = 0; // 轮播图
        const val TYPE_HOME_COURSE = 1; // 课程类型

    }
    var type = TYPE_HOME_DEFAULT

    override fun getItemType(): Int = type


    fun isDataSuccess(): Boolean {
        var isSuccess = false

        when (type) {
            TYPE_HOME_BANNER -> {
                // banner 类型
                if (banner_list != null && banner_list!!.isNotEmpty()) {

                    var bannerCount = 0

                    for (bannerBean in banner_list!!) {

                        bannerCount++
                    }
                    if (bannerCount == banner_list!!.size) {
                        isSuccess = true
                    }
                }
            }
            TYPE_HOME_COURSE -> {
                // 课程类型
                if (course != null && course!!.isNotEmpty()) {
                    var courseCount = 0;
                    for (subjectBean in course!!) {
                        if (subjectBean.isDataSuccess()) {
                            courseCount++
                        } else {
                            break
                        }
                    }
                    if (courseCount == course!!.size) {
                        isSuccess = true
                    }

                }
            }

            TYPE_HOME_DEFAULT -> {
                isSuccess = true
            }
            else -> {

                // 默认类型：默认为false
                isSuccess = true
            }
        }
        return isSuccess
    }

}
