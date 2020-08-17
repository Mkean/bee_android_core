package com.bee.home.fragment.contract

import com.bee.android.common.base.IPresenter
import com.bee.android.common.base.IView
import com.bee.home.fragment.bean.HomeNativeBean

/**
 *@Description: 首页--整体页面--接口协议
 *
 */
interface HomeEntranceContract {

    interface View : IView {
        fun setServerData(data: List<HomeNativeBean>?)

        fun finishRefreshAndLoadMore();

        fun showErrorView(isShow: Boolean, code: String)

        fun isHasData():Boolean
    }

    interface Presenter : IPresenter<View> {

    }
}