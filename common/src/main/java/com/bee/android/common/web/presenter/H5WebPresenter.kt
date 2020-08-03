package com.bee.android.common.web.presenter

import androidx.lifecycle.LifecycleOwner
import com.bee.android.common.base.BasePresenter
import com.bee.android.common.web.contract.H5WebContract

/**
 *@Description:
 *
 */
class H5WebPresenter(owner: LifecycleOwner) : BasePresenter<H5WebContract.View>(owner), H5WebContract.Presenter {

    override fun getUserInfo() {
        TODO("Not yet implemented")
    }

    override fun shareAddGold(course_id: String, share_type: String, subject_id: String) {
        TODO("Not yet implemented")
    }
}