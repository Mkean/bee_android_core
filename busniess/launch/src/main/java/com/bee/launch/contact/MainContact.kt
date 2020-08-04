package com.bee.launch.contact

import com.bee.android.common.base.IPresenter
import com.bee.android.common.base.IView
import com.bee.android.common.bean.UpdateBean
import com.bee.launch.manager.HomeDialogType

/**
 *@Description:
 *
 */
interface MainContact {

    interface Presenter : IPresenter<View> {

        fun getAppUpdate(local_version: String, client_type: String)

        fun getProtocolTimeStamp()

    }

    interface View : IView {

        fun showSuccess(bean: UpdateBean?)

        fun showFail()

        fun dialogFinish(currentDialogType: HomeDialogType)

        fun updateProtocol(userDataAgreementTime: Long, userRegisterAgreementTime: Long)
    }
}