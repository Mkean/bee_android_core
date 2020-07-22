package com.bee.update.flow;

import android.app.Activity;
import android.app.Dialog;

import com.bee.android.common.base.CommonApplication;
import com.bee.core.logger.CommonLogger;
import com.bee.update.UpdateBuilder;
import com.bee.update.base.DownloadCallback;
import com.bee.update.base.DownloadWorker;
import com.bee.update.base.InstallNotifier;
import com.bee.update.model.Update;
import com.bee.update.util.ActivityManager;
import com.bee.update.util.SafeDialogHandle;
import com.bee.update.util.Utils;

import java.io.File;

/**
 * @Description: 默认下载任务的回调监听，主要用于接收从{@link DownloadWorker}中传递过来的下载状态，通知用户并触发后续流程
 */
public class DefaultDownloadCallback implements DownloadCallback {

    private final String TAG = "Update";

    private UpdateBuilder builder;
    // 通过UpdateConfig或者UpdateBuilder所设置的下载回调监听。通过此监听器进行通知用户下载状态
    private DownloadCallback callback;
    private Update update;
    // 通过DownloadCreator所创建的回调监听，通过此监听器进行下载通知的UI更新
    private DownloadCallback innerCB;
    //下载失败 ，点击重试 通知
    private DownloadCallback errorCB;

    public void setBuilder(UpdateBuilder builder) {
        this.builder = builder;
        callback = builder.getDownloadCallback();
    }

    public void setUpdate(Update update) {
        this.update = update;
    }

    @Override
    public void onDownloadStart() {
        CommonLogger.e(TAG, "apk下载", "创建通知");
        try {
            if (callback != null) {
                callback.onDownloadStart();
            }
            if (!update.isForced()) {
                innerCB = getInnerCB();
                if (innerCB != null) {
                    innerCB.onDownloadStart();
                }
            }

            errorCB = getErrorCB();
        } catch (Throwable t) {
            onDownloadError(t);
        }
    }

    private DownloadCallback getInnerCB() {
        if (innerCB != null || !builder.getUpdateStrategy().isShowDownloadDialog()) {
            return innerCB;
        }

        Activity current = CommonApplication.store.getLast();
        if (Utils.isValid(current)) {
            CommonLogger.e(TAG, "apk下载", "主Activity");

            innerCB = builder.getDownloadNotifier().bind(builder, update).create(update, current);
        }
        return innerCB;
    }

    private DownloadCallback getErrorCB() {

        Activity current = CommonApplication.store.getLast();
        errorCB = builder.getDownloadErrorNotifier().bind(builder, update).create(update, current);

        if (Utils.isValid(current)) {
            CommonLogger.e(TAG, "apk下载 下载失败，点击重试通知创建", "主Activity");
        }
        return errorCB;
    }

    @Override
    public void onDownloadComplete(File file) {
        try {
            if (callback != null) {
                callback.onDownloadComplete(file);
                CommonLogger.d(TAG, "callback---下载完成");
            }

            if (innerCB != null) {
                innerCB.onDownloadComplete(file);
                CommonLogger.d(TAG, "innerCB---下载完成");
            }
        } catch (Throwable t) {
            onDownloadError(t);
        }
    }

    public void postForInstall(final File file) {
        CommonLogger.d(TAG, "安装本地文件apk");
        final UpdateBuilder updateBuilder = this.builder;
        Utils.getMainHandler().post(() -> {
            if (!update.isForced()) {//非强制更新才弹通知
                InstallNotifier notifier = updateBuilder.getInstallNotifier();
                notifier.setBuilder(updateBuilder);
                notifier.setUpdate(update);
                notifier.setFile(file);
                Activity current = ActivityManager.get().topActivity();
                CommonLogger.d(TAG, "current: " + current.getClass().getSimpleName());
                if (Utils.isValid(current)
                        && !builder.getUpdateStrategy().isAutoInstall()) {
                    CommonLogger.d(TAG, "弹窗安装apk");
                    Dialog dialog = notifier.create(current);
                    SafeDialogHandle.safeShowDialog(dialog);
                } else {
                    CommonLogger.d(TAG, "吊起安装");
                    notifier.sendToInstall();
                }
            }

        });
    }

    @Override
    public void onDownloadProgress(long current, long total) {
        try {
            if (callback != null) {
                callback.onDownloadProgress(current, total);
                CommonLogger.d(TAG, "callback------current" + current, "total" + total);
            }
            if (!update.isForced()) {
                if (innerCB != null) {
                    innerCB.onDownloadProgress(current, total);
                    CommonLogger.d(TAG, "innerCB------current" + current, "total" + total);
                }
            }
        } catch (Throwable t) {
            onDownloadError(t);
        }

    }

    @Override
    public void onDownloadError(Throwable t) {
        try {
            if (callback != null) {
                callback.onDownloadError(t);
                CommonLogger.d(TAG, "callback------current" + t, t.getMessage());
            }
            if (innerCB != null) {
                innerCB.onDownloadError(t);
                CommonLogger.d(TAG, "callback------innerCB" + t, t.getMessage());
            }

            if (!update.isForced()) {
                if (errorCB != null) {
                    errorCB.onDownloadError(t);
                    CommonLogger.d(TAG, "callback------errorCB" + update.isForced() + t, t.getMessage());
                }
            }

        } catch (Throwable e) {
            e.printStackTrace();
        }

        CommonLogger.printErrStackTrace(TAG,t,"更新失败");
    }
}
