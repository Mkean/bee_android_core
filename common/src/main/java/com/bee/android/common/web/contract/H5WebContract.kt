package com.bee.android.common.web.contract

import com.bee.android.common.base.IPresenter
import com.bee.android.common.base.IView

/**
 *@Description: H5 页面 contract
 *
 */
interface H5WebContract {

    interface View : IView {
        fun shareSuccess()

        fun shareFailure(message: String)
    }

    interface Presenter : IPresenter<View> {
        /**
         * 获取用户信息
         */
        fun getUserInfo()

        /**
         * 分享添加香蕉币
         */
        fun shareAddGold(course_id: String, share_type: String, subject_id: String)
    }

}