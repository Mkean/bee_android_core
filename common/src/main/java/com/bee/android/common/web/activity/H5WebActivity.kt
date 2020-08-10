package com.bee.android.common.web.activity

import com.alibaba.android.arouter.facade.annotation.Route
import com.bee.android.common.web.config.H5WebConfig.H5_ROUTER_WEB_ACTIVITY
import com.bee.android.common.web.contract.H5WebContract
import com.bee.android.common.web.presenter.H5WebPresenter

/**
 *@Description:
 *
 */
@Route(path = H5_ROUTER_WEB_ACTIVITY)
class H5WebActivity : WebBaseActivity<H5WebPresenter>(), H5WebContract.View {

    override fun getPresenter(): H5WebPresenter {
        return H5WebPresenter(this)
    }

    override fun shareSuccess() {
        TODO("Not yet implemented")
    }

    override fun shareFailure(message: String) {
        TODO("Not yet implemented")
    }
}