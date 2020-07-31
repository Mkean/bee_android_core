package com.bee.launch.presenter

import android.text.TextUtils
import androidx.lifecycle.LifecycleOwner
import com.bee.android.common.base.BasePresenter
import com.bee.android.common.bean.UpdateBean
import com.bee.android.common.network.BaseResp
import com.bee.android.common.network.rxjava.BaseTranFormer
import com.bee.android.common.network.rxjava.ResultCallback
import com.bee.core.logger.CommonLogger
import com.bee.core.network.API
import com.bee.core.network.rxjava.transformer.DataMainTransformer
import com.bee.core.utils.GsonUtil
import com.bee.launch.LaunchApi
import com.bee.launch.contact.MainContact
import com.rxjava.rxlife.RxLife

/**
 *@Description:
 *
 */

class MainPresenter(owner: LifecycleOwner) : BasePresenter<MainContact.View>(owner), MainContact.Presenter {

    companion object {
        const val TAG = "MainPresenter"
    }

    override fun getProtocolTimeStamp() {
        CommonLogger.i(TAG, "请求AppConfig接口")

        API.service(LaunchApi::class.java)
                .getProtocolTimeStamp()
                .compose(BaseTranFormer())
                .`as`(RxLife.`as`(this))
                .subscribe(object : ResultCallback<Map<String, String>>() {
                    override fun onSuccess(map: Map<String, String>?) {
                        var userDataAgreementTime: Long = 0
                        var userRegisterAgreementTime: Long = 0

                        if (map != null) {
                            CommonLogger.i(TAG, map.toString())

                            val userDataAgreement = if (map.containsKey("userDataAgreement")) map["userDataAgreement"] else ""
                            val userRegisterAgreement = if (map.containsKey("userRegisterAgreement")) map["userRegisterAgreement"] else ""

                            if (!TextUtils.isEmpty(userDataAgreement)) {
                                try {
                                    if (userDataAgreement != null) {
                                        userDataAgreementTime = userDataAgreement.toLong()
                                    }
                                } catch (e: Exception) {
                                    CommonLogger.printErrStackTrace(TAG, e, "解析隐私协议更新时间异常")
                                }
                            }

                            if (!TextUtils.isEmpty(userRegisterAgreement)) {
                                try {
                                    if (userRegisterAgreement != null) {
                                        userRegisterAgreementTime = userRegisterAgreement.toLong()
                                    }
                                } catch (e: Exception) {
                                    CommonLogger.printErrStackTrace(TAG, e, "解析注册协议更新时间异常")
                                }
                            }
                        }

                        if (mView != null) {
                            mView.updateProtocol(userDataAgreementTime, userRegisterAgreementTime);
                        }
                    }

                    override fun onFailure(e: Throwable?) {
                        super.onFailure(e)
                        if (mView != null) {
                            mView.updateProtocol(0, 0);
                        }
                        CommonLogger.printErrStackTrace(TAG, e, "请求AppConfig接口failed")
                    }

                })

    }

    override fun getAppUpdate(local_version: String, client_type: String) {

        CommonLogger.i(TAG, "请求版本升级接口，local_version={},client.type={}", local_version, client_type)

        API.service(LaunchApi::class.java)
                .updateApk(local_version, client_type)
                .compose(DataMainTransformer())
                .`as`(RxLife.`as`(this))
                .subscribe(object : ResultCallback<BaseResp<UpdateBean>>() {
                    override fun onSuccess(bean: BaseResp<UpdateBean>?) {

                        if (bean != null && mView != null) {
                            CommonLogger.i(TAG, "版本升级接口数据={}", GsonUtil.getInstance().objToJson(bean))
                            if (isSuccessCode(bean.code)) {
                                mView.showSuccess(bean.data)
                            } else {
                                mView.showFail()
                            }

                        }
                    }

                    override fun onFailure(e: Throwable?) {
                        super.onFailure(e)
                        if (mView != null) {
                            mView.showFail()
                        }
                        CommonLogger.printErrStackTrace(TAG, e, "版本升级接口调取failed")
                    }

                })
    }


    fun isSuccessCode(code: String): Boolean {
        if (TextUtils.isEmpty(code)) {
            return false
        }
        return "200" == code
    }
}