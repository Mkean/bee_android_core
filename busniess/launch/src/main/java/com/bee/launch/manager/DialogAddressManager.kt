package com.bee.launch.manager

import android.content.Context
import com.alibaba.android.arouter.launcher.ARouter
import com.bee.android.common.dialog.BaseDialog
import com.bee.android.common.dialog.TitleMsgTwoButtonDialog
import com.bee.android.common.mmkv.MMKVUtils
import com.bee.android.common.network.BaseResp
import com.bee.android.common.network.rxjava.BaseTranFormer
import com.bee.android.common.network.rxjava.ResultCallback
import com.bee.android.common.utils.UserUtils
import com.bee.android.common.web.config.H5WebConfig
import com.bee.core.logger.CommonLogger
import com.bee.core.network.API
import com.bee.core.utils.TimeUtil
import com.bee.launch.bean.AddressNoticeBean
import com.bee.launch.constant.LAUNCH_MMKV_ID
import io.reactivex.Observable
import retrofit2.http.GET

/**
 *@Description: 展示提醒用户去填写收货地址的弹窗管理
 *
 */
class DialogAddressManager {

    companion object {
        private val TAG = this.javaClass.simpleName
    }

    private lateinit var KEY_TIME: String
    private var addressDialog: TitleMsgTwoButtonDialog? = null
    private var listener: DialogAddressManagerListener? = null

    fun showCheckDialog(context: Context, listener: DialogAddressManagerListener) {
        CommonLogger.e(TAG, "showCheckDialog")

        this.listener = listener

        if (isShowDialog()) {
            getAddress(context)
        }
    }

    /**
     * 第一步判断是否登录 和 时间判断
     *
     */
    private fun isShowDialog(): Boolean {
        CommonLogger.e(TAG, "用户是否登录")

        if (!UserUtils.getInstance().isUserLoginValid) {
            // 如果用户未登录，则不提醒用户
            if (listener != null) {
                listener!!.nativeFinish(true)
            }
            return false
        }

        // 判断上次展示的时间
        KEY_TIME = "dialog_address_manager_time" + UserUtils.getInstance().user_id
        val time: Long = MMKVUtils.getLong(LAUNCH_MMKV_ID, KEY_TIME, 0);
        val isTime: Boolean = TimeUtil.isLargeOneDay(time, System.currentTimeMillis())
        CommonLogger.e(TAG, "时间是否显示：$isTime")

        if (!isTime) {
            if (listener != null) {
                listener!!.nativeFinish(false)
            }
        }
        return isTime
    }

    /**
     * 第二步：请求接口，是否显示
     *
     * @param context
     */
    private fun getAddress(context: Context) {
        CommonLogger.e(TAG, "请求地址提醒接口")

        API.service(DialogAddressManagerApi::class.java)
                .getAddress()
                .compose(BaseTranFormer())
                .subscribe(object : ResultCallback<AddressNoticeBean>() {

                    override fun onSuccess(bean: AddressNoticeBean?) {
                        if (bean != null) {
                            CommonLogger.e(TAG, "请求地址提醒接口成功==$bean")
                            if (bean.is_need) {
                                // 弹窗
                                showDialog(context, bean)
                            } else {
                                // 不弹窗
                                if (listener != null) {
                                    listener!!.nativeFinish(false)
                                }
                            }
                        }
                    }

                    override fun onFailure(e: Throwable?) {
                        super.onFailure(e)

                        if (listener != null) {
                            listener!!.nativeFinish(true)
                        }
                        CommonLogger.printErrStackTrace(TAG, e, "请求地址提醒接口失败")
                    }
                })
    }

    /**
     * 第三步：显示dialog
     *
     * @param context
     * @param bean
     */
    private fun showDialog(context: Context, bean: AddressNoticeBean) {
        if (addressDialog == null) {
            addressDialog = TitleMsgTwoButtonDialog(context,
                    bean.top,
                    bean.center,
                    "取消", bean.footer
                    , object : BaseDialog.ActionListener {
                override fun clickLeft() {
                    if (listener != null) {
                        listener!!.nativeFinish(false)
                    }
                }

                override fun clickRight() {
                    ARouter.getInstance().build(H5WebConfig.H5_ROUTER_WEB_ACTIVITY)
                            .withString(H5WebConfig.H5_PARAM_URL, H5WebConfig.H5_URL_ORDER_LIST)
                            .withString(H5WebConfig.H5_PARAM_FLAG, H5WebConfig.H5_FLAG_ORDER_LIST)
                            .withString(H5WebConfig.H5_PARAM_DESC, "首页地址提醒弹窗")
                            .navigation()

                    if (listener != null) {
                        listener!!.nativeFinish(false)
                    }

                }

            })
            addressDialog!!.setCanceledOnTouchOutsideAndKeyBack(false, false)
        }
        addressDialog!!.show()
        CommonLogger.e(TAG, "显示地址提醒dialog")
        MMKVUtils.putLong(LAUNCH_MMKV_ID, KEY_TIME, System.currentTimeMillis())
    }

     interface DialogAddressManagerApi {

        /**
         * 订单用户是否有地址
         *
         * <p>
         *    http://yapi.talbrain.com/mock/807/order/is_need_full_address
         */
        @GET("shop/order/is_need_full_address")
        fun getAddress(): Observable<BaseResp<AddressNoticeBean>>
    }

     interface DialogAddressManagerListener {

        fun nativeFinish(isAgain: Boolean);
    }
}