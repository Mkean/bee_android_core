package com.bee.android.common.third.share;

import android.content.Intent;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

/**
 * TODO：未完成 。微信分享类
 */
public enum WxUtil implements IWXAPIEventHandler {

    INSTANCE;

    private IWXAPI api;


    /**
     * 接收微信返回页面 onActivityResult 方法实现
     *
     * @param data
     */
    public void entryResult(Intent data) {
        if (api != null) {
            api.handleIntent(data, this);
        }
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {

    }
}
