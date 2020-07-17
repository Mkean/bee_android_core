package com.bee.update.base;

import com.bee.android.common.logger.CommonLogger;
import com.bee.update.UpdateBuilder;
import com.bee.update.flow.DefaultDownloadCallback;
import com.bee.update.impl.DefaultDownloadWorker;
import com.bee.update.model.Update;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * <b>核心操作类</b>
 * <p>
 * 此为下载任务的封装基类，主要用于对下载中的进度、状态进行派发，以起到连接更新流程的作用
 */
public abstract class DownloadWorker implements Runnable {

    public final String TAG = "Update";

    /**
     * {@link DefaultDownloadCallback} 的实例，用于接收下载状态并进行后续流程通知
     */
    private DefaultDownloadCallback callback;

    // 维护一个所有下载任务的下载文件集合，用于避免出现多个下载任务同时下载同一个文件。避免冲突。
    public static Map<DownloadWorker, File> downloading = new HashMap<>();

    protected Update update;
    protected UpdateBuilder builder;

    public final void setUpdate(Update update) {
        this.update = update;
    }

    public final void setUpdateBuilder(UpdateBuilder builder) {
        this.builder = builder;
    }

    public final void setCallback(DefaultDownloadCallback callback) {
        this.callback = callback;
    }

    @Override
    public void run() {
        try {
            File file = builder.getFileCreator().createWithBuilder(update, builder);
            FileChecker checker = builder.getFileChecker();
            CommonLogger.d(TAG, "开始下载文件将要存储的路径：" + file.getAbsolutePath());
            // TODO：未完成

        } catch (Throwable t) {

        }
    }
}
