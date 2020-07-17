package com.bee.update.base;

import com.bee.update.UpdateBuilder;
import com.bee.update.impl.DefaultRestartHandler;
import com.bee.update.model.Update;

import java.io.File;

/**
 * 后台重启任务定制接口。此接口用于定制后台任务的重启逻辑，可参考{@link DefaultRestartHandler}
 */
public class RestartHandler implements CheckCallback, DownloadCallback {

    protected UpdateBuilder builder;
    protected long retryTime; // 重启间隔

    public final void attach(UpdateBuilder builder, long retryTime) {
        this.builder = builder;
        this.retryTime = Math.max(1, retryTime);
    }

    public final void detach() {
        builder = null;
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
}
