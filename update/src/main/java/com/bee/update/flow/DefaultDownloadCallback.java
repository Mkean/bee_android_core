package com.bee.update.flow;

import com.bee.update.base.DownloadCallback;
import com.bee.update.base.DownloadWorker;

import java.io.File;

/**
 * @Description: 默认下载任务的回调监听，主要用于接收从{@link DownloadWorker}中传递过来的下载状态，通知用户并触发后续流程
 */
public class DefaultDownloadCallback implements DownloadCallback {
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
