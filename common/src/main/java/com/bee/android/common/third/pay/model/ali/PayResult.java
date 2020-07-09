package com.bee.android.common.third.pay.model.ali;

/**
 * 支付结果界面
 * TODO：继续
 */
public class PayResult {

    private String resultStatus;
    private String result;
    private String memo;

    private String getValue(String content, String key) {
        String prefix = key + "={";
        return content.substring(content.indexOf(prefix) + prefix.length(), content.lastIndexOf("}"));
    }
}
