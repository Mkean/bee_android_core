package com.bee.home.fragment.bean

import com.bee.core.logger.CommonLogger
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

    companion object {

        const val TYPE_SERVER_HOME_COURSE_EXPERIENCE = "1" // String 比较用的，体验课类型
        const val TYPE_SERVER_HOME_COURSE_SYSTEM = "2" // String 比较用的，系统课类型

        const val TYPE_HOME_COURSE_DEFAULT = -1 // 默认类型
        const val TYPE_HOME_COURSE_EXPERIENCE = 1 // 体验课类型
        const val TYPE_HOME_COURSE_SYSTEM = 2 // 系统课类型
    }

    override fun getItemType(): Int {

        return when (course_type) {
            TYPE_SERVER_HOME_COURSE_EXPERIENCE -> {
                TYPE_HOME_COURSE_EXPERIENCE
            }
            TYPE_SERVER_HOME_COURSE_SYSTEM -> {
                TYPE_HOME_COURSE_SYSTEM
            }
            else -> {
                TYPE_HOME_COURSE_DEFAULT
            }
        }
    }

    fun isDataSuccess(): Boolean {
        val isSuccess: Boolean
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

    override fun toString(): String {
        return "HomeSubjectItemBean(course_type='$course_type', experience_course=$experience_course, system_course=$system_course, TYPE_SERVER_HOME_COURSE_EXPERIENCE='$TYPE_SERVER_HOME_COURSE_EXPERIENCE', TYPE_SERVER_HOME_COURSE_SYSTEM='$TYPE_SERVER_HOME_COURSE_SYSTEM')"
    }


}
