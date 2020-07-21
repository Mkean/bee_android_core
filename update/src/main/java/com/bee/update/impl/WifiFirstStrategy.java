package com.bee.update.impl;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.bee.update.base.UpdateStrategy;
import com.bee.update.model.Update;
import com.bee.update.util.ActivityManager;

/**
 * 默认提供的更新策略：
 * 1.当处于WiFi环境时，只展示下载完成后的通知
 * 2.在处于非WiFi环境时，只展示有新版本更新及下载进度的通知
 */
public class WifiFirstStrategy extends UpdateStrategy {

    private boolean isWifi;


    @Override
    public boolean isShowUpdateDialog(Update update) {
        isWifi = isConnectedByWifi();
        return !isWifi;
    }

    @Override
    public boolean isAutoInstall() {
        return !isWifi;
    }

    @Override
    public boolean isShowDownloadDialog() {
        return !isWifi;
    }

    private boolean isConnectedByWifi() {
        Context context = ActivityManager.get().getApplicationContext();
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connManager.getActiveNetworkInfo();
        return info != null
                && info.isConnected()
                && info.getType() == ConnectivityManager.TYPE_WIFI;
    }
}
