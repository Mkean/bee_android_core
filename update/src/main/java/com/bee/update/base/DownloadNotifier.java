package com.bee.update.base;

import android.app.Activity;

import com.bee.update.UpdateBuilder;
import com.bee.update.UpdateConfig;
import com.bee.update.flow.Launcher;
import com.bee.update.impl.DefaultDownloadNotifier;
import com.bee.update.model.Update;
import com.bee.update.util.ActivityManager;

/**
 * apk 下载任务的通知创建器
 *
 * <p>配置方式：通过{@link UpdateConfig#setDownloadNotifier(DownloadNotifier)}或者{@link UpdateBuilder#setDownloadNotifier(DownloadNotifier)}
 *
 * <p>默认实现 {@link DefaultDownloadNotifier}
 *
 * <p> 当配置的更新分类{@link  UpdateStrategy#isShowDownloadDialog()}设置为true时，此通知创建器将被触发。
 */
public abstract class DownloadNotifier {
    protected Update update;
    protected UpdateBuilder builder;

    public final DownloadNotifier bind(UpdateBuilder builder, Update update) {
        this.update = update;
        this.builder = builder;
        return this;
    }

    protected final void restartDownload() {
        Launcher.getInstance().launchDownload(update, builder);
    }

    /**
     * 创建一个下载任务的下载进度回调。此回调将用于接收下载任务的状态并更新UI。
     *
     * @param update   更新数据实体类
     * @param activity 顶部的Activity实例。通过{@link ActivityManager#topActivity()}进行获取
     * @return 被创建的回调器。允许为null。
     */
    public abstract DownloadCallback create(Update update, Activity activity);

}
