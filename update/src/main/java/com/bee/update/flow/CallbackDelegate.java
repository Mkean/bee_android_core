package com.bee.update.flow;

import com.bee.update.base.CheckCallback;
import com.bee.update.base.DownloadCallback;
import com.bee.update.base.RestartHandler;
import com.bee.update.model.Update;

import java.io.File;

/**
 * 更新回调的代理类，用于统一进行回调分发，并打印日志辅助调试
 */
public class CallbackDelegate implements CheckCallback, DownloadCallback {
    private CheckCallback checkProxy;
    private DownloadCallback downloadProxy;
    private RestartHandler restartHandler;

    public void setCheckDelegate(CheckCallback checkProxy) {
        this.checkProxy = checkProxy;
    }

    public void setDownloadDelegate(DownloadCallback downloadProxy) {
        this.downloadProxy = downloadProxy;
    }

    @Override
    public void onCheckStart() {

    }

    @Override
    public void hasUpdate(Update update) {

    }

    @Override
    public void noUpdate() {

    }

    @Override
    public void onCheckError(Throwable e) {

    }

    @Override
    public void onUserCancel() {

    }

    @Override
    public void onCheckIgnore(Update update) {

    }

    @Override
    public void onDownloadStart() {

    }

    @Override
    public void onDownloadComplete(File file) {

    }

    @Override
    public void onDownloadProgress(long current, long total) {

    }

    @Override
    public void onDownloadError(Throwable t) {

    }

    public void setRestartHandler(RestartHandler restartHandler) {
        this.restartHandler = restartHandler;
    }
}
