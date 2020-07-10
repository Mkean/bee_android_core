package com.bee.android.common.third.pay;

public interface PayListener {
    int ERROR_SING = 0; // 签名错误
    int ERROR_PARAM = 1; // 参数错误
    int ERROR_CONFIRM = 2; // 确认中
    int ERROR_CANCEL = 3; // 取消支付
    int ERROR_OTHER = 4; // 其他原因
    int ERROR_NO_WECHAT = 5; // 没有安装微信

    /**
     * 成功
     *
     * @param type
     */
    void onPaySuccess(PayAgent.PayType type);

    /**
     * 失败
     *
     * @param type
     * @param code      转译后错误码
     * @param msg       转译后错误信息
     * @param errorCode 支付SDK错误码
     * @param errorMsg  支付SDK错误信息
     */
    void onPayFail(PayAgent.PayType type, int code, String msg, String errorCode, String errorMsg);
}
