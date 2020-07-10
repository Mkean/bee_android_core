package com.bee.android.ai.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.bee.android.common.BuildConfig;
import com.bee.android.common.third.pay.PayAgent;
import com.bee.android.common.third.pay.PayListener;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private IWXAPI api;

    public static PayListener getPayListener() {
        return payListener;
    }

    public static void setPayListener(PayListener payListener) {
        WXPayEntryActivity.payListener = payListener;
    }

    public static PayListener payListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        api = WXAPIFactory.createWXAPI(this, BuildConfig.WXAPPID, true);
        api.handleIntent(getIntent(), this);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp resp) {
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            if (payListener != null) {
                if (resp.errCode == 0) {
                    payListener.onPaySuccess(PayAgent.PayType.WECHATPAY);
                } else if (resp.errCode == -2) {
                    payListener.onPayFail(PayAgent.PayType.WECHATPAY, PayListener.ERROR_CANCEL, "取消支付", String.valueOf(resp.errCode), toStr(resp));
                } else {
                    payListener.onPayFail(PayAgent.PayType.WECHATPAY, PayListener.ERROR_OTHER, "支付失败", String.valueOf(resp.errCode), toStr(resp));
                }
            }
        } else {
            if (payListener != null) {
                payListener.onPayFail(PayAgent.PayType.WECHATPAY, PayListener.ERROR_OTHER, "", String.valueOf(resp.errCode), toStr(resp));
            }
        }

        // 防止立即关闭黑屏
        new Handler().postDelayed(() -> finish(), 200);
    }

    private String toStr(BaseResp resp) {
        if (resp == null) {
            return "";
        }
        return "errCode=" + resp.errCode
                + ",errStr=" + (TextUtils.isEmpty(resp.errStr) ? "" : resp.errStr)
                + ",openId=" + (TextUtils.isEmpty(resp.openId) ? "" : resp.openId)
                + ",transaction=" + (TextUtils.isEmpty(resp.transaction) ? "" : resp.transaction
                + ",type=" + resp.getType());
    }
}
