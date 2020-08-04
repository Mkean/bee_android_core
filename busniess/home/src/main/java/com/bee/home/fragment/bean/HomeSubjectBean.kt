package com.bee.home.fragment.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 *@Description: 首页---学科bean对象
 *
 */
data class HomeSubjectBean(
    @SerializedName("subject_name")
    val subject_name: String, // 语文 / 思维 / 英语

    @SerializedName("subject_id")
    val subject_id: String, // 1 语文 2 思维 3  英语

    @SerializedName("courses")
    val courses: List<HomeSubjectItemBean>?=null // 学科内容数组

) : Serializable {

    fun isDataSuccess(): Boolean {
        var isSuccess = false

        if (courses != null && courses.isNotEmpty()) {
            var count = 0

            for (itemBean in courses) {
                if (itemBean.isDataSuccess()) {
                    // 成功一次就计数加一
                    count++
                } else {
                    // 失败一次，就整体失败，有一个没数据，就认为整体数据出错
                    break
                }
            }

            if (count == courses.size) {
                isSuccess = true
            }
        }
        return isSuccess
    }
}