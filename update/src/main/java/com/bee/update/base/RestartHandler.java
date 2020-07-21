package com.bee.update.base;

import com.bee.android.common.logger.CommonLogger;
import com.bee.update.UpdateBuilder;
import com.bee.update.impl.DefaultRestartHandler;
import com.bee.update.model.Update;
import com.bee.update.util.L;
import com.bee.update.util.Utils;

import java.io.File;

/**
 * 后台重启任务定制接口。此接口用于定制后台任务的重启逻辑，
 * <p>
 * 默认实现：{@link DefaultRestartHandler}
 */
public abstract class RestartHandler implements CheckCallback, DownloadCallback {

    protected UpdateBuilder builder;
    protected long retryTime;// 重启间隔
    private RetryTask task;

    public final void attach(UpdateBuilder builder, long retryTime) {
        this.builder = builder;
        this.retryTime = Math.max(1, retryTime);
    }

    public final void detach() {
        builder = null;
    }

    protected final void retry() {
        if (builder == null) {
            return;
        }
        if (task == null) {
            task = new RetryTask();
        }
        Utils.getMainHandler().removeCallbacks(task);
        Utils.getMainHandler().postDelayed(task, retryTime * 1000);
    }

    private class RetryTask implements Runnable {


        @Override
        public void run() {
            if (builder != null) {
                L.d("RetryTask", "Restart update for daemon");
                builder.checkWithDaemon(retryTime);
            }
        }
    }

    // ======所有生命周期回调均为空实现，由子类复写相应的生命周期函数进行重启操作======

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
