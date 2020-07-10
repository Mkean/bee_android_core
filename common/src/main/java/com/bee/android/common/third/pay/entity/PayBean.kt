package com.bee.android.common.third.pay.entity

data class PayBean(
         val partnerId: String, // 商户号
         val prepayId: String, // 预付单号
         val nonceStr: String, // 随机串
         val timeStamp: String, // 时间戳
         val sign: String, // 签名
         val channel: String, // 支付类型
         val order: String // 订单信息


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