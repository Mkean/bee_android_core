package com.bee.update.impl;

import android.content.Context;

import com.bee.update.base.FileCreator;
import com.bee.update.model.Update;
import com.bee.update.util.ActivityManager;

import java.io.File;

/**
 * 默认使用的APK下载文件创建器。
 */
public class DefaultFileCreator extends FileCreator {
    @Override
    public File create(Update update) {
        File cacheDir = getCacheDir();
        cacheDir.mkdirs();
        return new File(cacheDir, "update_normal_" + update.getVersionName());
    }

    @Override
    public File createForDaemon(Update update) {
        File cacheDir = getCacheDir();
        cacheDir.mkdirs();
        return new File(cacheDir, "update_daemon_" + update.getVersionName());
    }

    private File getCacheDir() {
        Context context = ActivityManager.get().getApplicationContext();
        File cacheDir = context.getExternalCacheDir();
        if (cacheDir == null) {
            cacheDir = context.getCacheDir();
        }
        cacheDir = new File(cacheDir, "update");
        return cacheDir;
    }
}
