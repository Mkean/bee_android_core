package com.bee.android.common.third.pay;

/**
 * 支付方式
 */
public class PayAgent {
    /**
     * 支付方式
     */
    public enum PayType {
        /**
         * 支付宝
         */
        ALIPAY,
        /**
         * 微信
         */
        WECHATPAY,
        /**
         * 银联支付
         */
        UPPAY
    }
}
