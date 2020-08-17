package com.bee.home.fragment.adapter

import android.util.SparseArray
import com.bee.core.logger.CommonLogger
import com.bee.home.R
import com.bee.home.fragment.bean.HomeSubjectItemBean
import com.bee.home.fragment.widget.HomeCoursePageAdapterExperienceView
import com.bee.home.fragment.widget.HomeCoursePageAdapterSystemView
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 *@Description:首页---学科---加载课程卡片 使用的adapter
 *
 */
class HomeCourseItemAdapter(data: List<HomeSubjectItemBean>?) :
    BaseMultiItemQuickAdapter<HomeSubjectItemBean, BaseViewHolder>(data) {

    private var subjectName: String = ""
    private var experienceViewMap: SparseArray<HomeCoursePageAdapterExperienceView>? = null


    init {

        addItemType(
            HomeSubjectItemBean.TYPE_HOME_COURSE_DEFAULT,
            R.layout.home_fragment_item_home_default
        ); // 默认


        addItemType(
            HomeSubjectItemBean.TYPE_HOME_COURSE_EXPERIENCE,
            R.layout.home_fragment_view_course_item_experience
        )

        addItemType(
            HomeSubjectItemBean.TYPE_HOME_COURSE_SYSTEM,
            R.layout.home_fragment_view_course_item_system
        )

        initExperienceViewList()

    }

    fun setNewData(data: MutableList<HomeSubjectItemBean>?, subjectName: String) {
        super.setNewData(data)
        this.subjectName = subjectName
        initExperienceViewList()
    }

    override fun convert(helper: BaseViewHolder?, item: HomeSubjectItemBean?) {
        if (item != null) {
            CommonLogger.e("HomeCourseItemAdapter","item != null   \n  ${item.toString()}")
            CommonLogger.e("HomeCourseItemAdapter","item.itemType==${item.itemType}")
            when (item.itemType) {
                HomeSubjectItemBean.TYPE_HOME_COURSE_EXPERIENCE -> {
                    // 体验课
                    val experienceView: HomeCoursePageAdapterExperienceView? =
                        helper?.getView(R.id.home_course_experience_layout)
                    experienceView?.setPosition(data.indexOf(item))
                    experienceView?.setData(item.experience_course, subjectName)

                    putExperienceView(experienceView)
                }

                HomeSubjectItemBean.TYPE_HOME_COURSE_SYSTEM -> {
                    // 系统课
                    val systemView: HomeCoursePageAdapterSystemView? =
                        helper?.getView(R.id.home_course_system_layout)
                    systemView?.setPosition(data.indexOf(item))

                    systemView?.setData(item.system_course, subjectName)

                }
            }
        }
    }

    private fun initExperienceViewList() {
        if (experienceViewMap != null) {
            experienceViewMap!!.clear()
            experienceViewMap = null
        }
        experienceViewMap = SparseArray()
    }

    private fun putExperienceView(view: HomeCoursePageAdapterExperienceView?) {
        if (experienceViewMap != null && view != null) {
            try {
                experienceViewMap!!.append(view.hashCode(), view)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun stopExperienceViewHeader() {
        if (experienceViewMap != null && experienceViewMap!!.size() > 0) {
            try {

                for (x in 0 until experienceViewMap!!.size()) {
                    experienceViewMap!!.get(experienceViewMap!!.keyAt(x))
                        ?.stopHeaderAndTextLayout();
                }
            } catch (E: Exception) {
                E.printStackTrace()
            }
        }
    }

    fun startExperienceViewHeader() {
        if (experienceViewMap != null && experienceViewMap!!.size() > 0) {
            for (x in 0 until experienceViewMap!!.size()) {
                try {
                    experienceViewMap!!.get(experienceViewMap!!.keyAt(x))
                        ?.startHeaderAndTextLayout()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}