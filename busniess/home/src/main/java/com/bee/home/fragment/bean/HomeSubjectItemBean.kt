package com.bee.home.fragment.bean

import com.bee.core.utils.StringUtils
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 *@Description:首页---课程卡片 bean对象
 *
 */
data class HomeSubjectItemBean(

    @SerializedName("course_type")
    val course_type: String, // 商品类型 1 体验课  2 系统课

    @SerializedName("experience_course")
    val experience_course: HomeSubjectExperienceBean?, // 体验课

    @SerializedName("system_course")
    val system_course: HomeSubjectSystemBean? // 系统课

) : Serializable, MultiItemEntity {

    private val TYPE_SERVER_HOME_COURSE_EXPERIENCE = "1" // String 比较用的，体验课类型
    private val TYPE_SERVER_HOME_COURSE_SYSTEM = "2" // String 比较用的，系统课类型

    companion object {
        const val TYPE_HOME_COURSE_DEFAULT = -1 // 默认类型
        const val TYPE_HOME_COURSE_EXPERIENCE = 1 // 体验课类型
        const val TYPE_HOME_COURSE_SYSTEM = 2 // 系统课类型
    }

    override fun getItemType(): Int {
        return when {
            TYPE_SERVER_HOME_COURSE_EXPERIENCE == course_type -> {
                TYPE_HOME_COURSE_EXPERIENCE
            }
            TYPE_SERVER_HOME_COURSE_SYSTEM == course_type -> {
                TYPE_HOME_COURSE_SYSTEM
            }
            else -> {
                TYPE_HOME_COURSE_DEFAULT
            }
        }
    }

    fun isDataSuccess(): Boolean {
        var isSuccess = false
        when (itemType) {
            TYPE_HOME_COURSE_EXPERIENCE -> {
                // 体验课
                isSuccess =
                    experience_course != null && !StringUtils.isNullOrEmpty(experience_course.course_type)
            }

            TYPE_HOME_COURSE_SYSTEM -> {
                // 系统课
                isSuccess =
                    system_course != null && !StringUtils.isNullOrEmpty(system_course.course_type)
            }
            TYPE_HOME_COURSE_DEFAULT -> {
                // 默认
                isSuccess = true
            }
            else -> {
                isSuccess = true
            }
        }
        return isSuccess
    }

}
