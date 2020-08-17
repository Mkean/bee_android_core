package com.bee.home.fragment.contract

import com.bee.android.common.base.IPresenter
import com.bee.android.common.base.IView
import com.bee.home.fragment.bean.HomeSubjectItemBean

/**
 *@Description:
 *
 */
interface HomeCourseContract {

    interface View : IView {
        fun setSubjectData(itemBeans: ArrayList<HomeSubjectItemBean>?)
    }

    interface Presenter : IPresenter<View> {

    }
}