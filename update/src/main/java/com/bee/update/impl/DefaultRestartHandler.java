package com.bee.update.impl;

import com.bee.update.base.RestartHandler;
import com.bee.update.model.Update;

import java.io.File;

/**
 * 默认使用的更新任务重启操作类。
 */
public class DefaultRestartHandler extends RestartHandler {

    // =====复写对应的回调并进行任务重启=====

    @Override
    public void onDownloadComplete(File file) {
        retry();
    }

    @Override
    public void onDownloadError(Throwable t) {
        retry();
    }

    @Override
    public void noUpdate() {
        retry();
    }

    @Override
    public void onCheckError(Throwable e) {
        retry();
    }

    @Override
    public void onUserCancel() {
        retry();
    }

    @Override
    public void onCheckIgnore(Update update) {
        retry();
    }
}
