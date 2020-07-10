package com.bee.android.common.third.pay;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.bee.android.common.third.pay.entity.PayBean;
import com.bee.android.common.third.pay.model.ali.AliPay;
import com.bee.android.common.third.pay.model.wechat.WxPay;

/**
 * 支付相关
 */
public class PayOperate {
    public static final String TAG = "Pay";

    public void toPay(Activity activity, PayBean bean, PayListener listener) {
        if ("wxpay".equals(bean.getChannel())) { // 微信支付
            Log.i(TAG, "weChat pay");
            WxPay.getInstance().pay(activity, bean, new com.bee.android.common.third.pay.PayListener() {
                @Override
                public void onPaySuccess(PayAgent.PayType type) {
                    Toast.makeText(activity, "支付成功", Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "微信支付成功");
                    if (listener != null) {
                        listener.success();
                    }
                }

                @Override
                public void onPayFail(PayAgent.PayType type, int code, String msg, String errorCode, String errorMsg) {
                    Toast.makeText(activity, code == com.bee.android.common.third.pay.PayListener.ERROR_NO_WECHAT ? "没有安装微信" : msg, Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "微信支付失败>>>msg : " + msg + " code:" + code + " errorCode:" + errorCode + " errorMsg:" + errorMsg);
                }
            });
        } else if ("alipay".equals(bean.getChannel())) { // 支付宝支付
            Log.i(TAG, "ali pay");
            AliPay.getInstance().pay(activity, bean, new com.bee.android.common.third.pay.PayListener() {
                @Override
                public void onPaySuccess(PayAgent.PayType type) {
                    Toast.makeText(activity, "支付成功", Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "支付宝支付成功");
                    if (listener != null) {
                        listener.success();
                    }
                }

                @Override
                public void onPayFail(PayAgent.PayType type, int code, String msg, String errorCode, String errorMsg) {
                    Log.e(TAG, "支付宝支付失败>>>msg : " + msg + " code:" + code + " errorCode:" + errorCode + " errorMsg:" + errorMsg);
                }
            });
        }
    }

    public interface PayListener {
        void success();
    }
}
