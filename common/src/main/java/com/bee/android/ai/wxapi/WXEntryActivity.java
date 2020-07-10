package com.bee.android.ai.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.bee.android.common.third.share.WxUtil;

public class WXEntryActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WxUtil.INSTANCE.entryResult(getIntent());
        this.finish();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        WxUtil.INSTANCE.entryResult(intent);
        this.finish();
    }
}
