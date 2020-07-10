package com.bee.android.common.third.pay.model.wechat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.util.Log;

import com.bee.android.ai.wxapi.WXPayEntryActivity;
import com.bee.android.common.BuildConfig;
import com.bee.android.common.third.pay.PayAgent;
import com.bee.android.common.third.pay.PayListener;
import com.bee.android.common.third.pay.entity.PayBean;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 微信支付工具类
 */
public class WxPay {
    private PayReq req;
    private IWXAPI msgApi;

    private static volatile WxPay instance;

    public static WxPay getInstance() {
        if (null == instance) {
            synchronized (WxPay.class) {
                if (null == instance) {
                    instance = new WxPay();
                }
            }
        }
        return instance;
    }

    @SuppressLint("CheckResult")
    public void pay(Activity activity, PayBean bean, PayListener listener) {
        Log.d("weChatPayLog---", "微信支付开始");
        // 先判断是否安装微信
        if (isAppInstalled(activity, "com.tencent.mm")) {
            msgApi = WXAPIFactory.createWXAPI(activity, null);
            WXPayEntryActivity.setPayListener(listener);
            Log.d("weChatPayLog---", "微信支付生成支付参数");

            Observable.timer(1, TimeUnit.SECONDS)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Long>() {
                        @Override
                        public void accept(Long aLong) throws Exception {
                            genPayReq(bean);
                            Log.d("weChatPayLog---", "微信支付掉调起前");
                            sendPayReq();
                            Log.d("weChatPayLog---", "微信支付调起后");
                        }
                    });
        } else {
            if (listener != null) {
                listener.onPayFail(PayAgent.PayType.WECHATPAY, PayListener.ERROR_NO_WECHAT,
                        "没有安装微信", String.valueOf(PayListener.ERROR_NO_WECHAT), "没有安装微信");
            }
        }
    }

    private boolean isAppInstalled(Activity ac, String uri) {
        synchronized (ac) {
            PackageManager pm = ac.getPackageManager();
            boolean installed;
            try {
                pm.getPackageInfo(uri, PackageManager.GET_SIGNATURES);
                installed = true;
            } catch (PackageManager.NameNotFoundException e) {
                installed = false;
            }
            return installed;
        }

    }

    private void genPayReq(PayBean bean) {
        req = new PayReq();
        req.appId = BuildConfig.WXAPPID;
        req.partnerId = bean.getPartnerId();
        req.prepayId = bean.getPrepayId();
        req.packageValue = "Sign=WXPay";
        req.nonceStr = bean.getNonceStr();
        req.timeStamp = bean.getTimeStamp();
        req.sign = bean.getSign();
    }

    private void sendPayReq() {
        msgApi.registerApp(BuildConfig.WXAPPID);
        msgApi.sendReq(req);
    }
}
