package com.bee.android.common.third.pay.model.ali;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;

import com.alipay.sdk.app.PayTask;
import com.bee.android.common.third.pay.PayAgent;
import com.bee.android.common.third.pay.PayListener;
import com.bee.android.common.third.pay.entity.PayBean;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 支付宝支付工具类
 */
public class AliPay {

    private PayListener listener;

    private static volatile AliPay instance;

    private AliPay() {
    }

    public static AliPay getInstance() {
        if (null == instance) {
            synchronized (AliPay.class) {
                if (null == instance) {
                    instance = new AliPay();
                }
            }
        }
        return instance;
    }

    @SuppressLint("CheckResult")
    public String pay(Activity activity, PayBean payBean, PayListener listener) {
        this.listener = listener;
        String orderInfo = payBean.getOrder();
        Observable.just(orderInfo)
                .map(s -> {
                    PayTask payTask = new PayTask(activity);
                    return payTask.payV2(s, true);
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(stringStringMap -> {
                    PayResult payResult = new PayResult((stringStringMap));
                    String resultStatus = payResult.getResultStatus();
                    Log.d("alipay", payResult.toString());
                    // 判断resultStatus 为 “9000” 则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals("9000", resultStatus)) {
                        listener.onPaySuccess(PayAgent.PayType.ALIPAY);
                    } else if (TextUtils.equals(resultStatus, "6001")) {
                        listener.onPayFail(PayAgent.PayType.ALIPAY, PayListener.ERROR_CANCEL, "取消支付",
                                resultStatus, stringStringMap != null ? stringStringMap.toString() : "");
                    } else if (TextUtils.equals(resultStatus, "8000")) {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        listener.onPayFail(PayAgent.PayType.ALIPAY, PayListener.ERROR_CONFIRM, "等待确认中",
                                resultStatus, stringStringMap != null ? stringStringMap.toString() : "");
                    } else {
                        // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                        if (payResult != null) {
                            listener.onPayFail(PayAgent.PayType.ALIPAY, PayListener.ERROR_OTHER, "支付失败",
                                    resultStatus, stringStringMap != null ? stringStringMap.toString() : "");
                        }
                    }
                });
        return null;
    }
}
