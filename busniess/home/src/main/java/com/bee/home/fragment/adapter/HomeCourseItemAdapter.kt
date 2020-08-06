package com.bee.home.fragment.adapter

import android.util.SparseArray
import com.bee.home.fragment.bean.HomeSubjectItemBean
import com.bee.home.fragment.widget.HomeCoursePageAdapterExperienceView
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 *@Description: 首页---学科---加载课程卡片 使用的adapter
 *
 */
class HomeCourseItemAdapter(data: List<HomeSubjectItemBean>?) :
    BaseMultiItemQuickAdapter<HomeSubjectItemBean, BaseViewHolder>(data) {

    private var subjectName: String = ""
    private val experienceViewMap: SparseArray<HomeCoursePageAdapterExperienceView>? = null


    fun setNewData(data: List<HomeSubjectItemBean>, subjectName: String) {
        super.setNewData(data)
        this.subjectName = subjectName

        initExperienceViewList()
    }

    init {

        initExperienceViewList()
    }

    override fun convert(helper: BaseViewHolder?, item: HomeSubjectItemBean?) {

    }

    private fun initExperienceViewList() {
    }
}