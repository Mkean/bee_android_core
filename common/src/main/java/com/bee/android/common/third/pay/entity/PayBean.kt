package com.bee.android.common.third.pay.entity

data class PayBean(
        private val partnerId: String, // 商户号
        private val prepayId: String, // 预付单号
        private val nonceStr: String, // 随机串
        private val timeStamp: String, // 时间戳
        private val sign: String, // 签名
        private val channel: String, // 支付类型
        private val order: String // 订单信息


) {
    override fun toString(): String {
        return "PayBean(partnerId='$partnerId'," +
                " prepayId='$prepayId', " +
                "nonceStr='$nonceStr', " +
                "timeStamp='$timeStamp', " +
                "sign='$sign', " +
                "channel='$channel'," +
                " order='$order')"
    }
}