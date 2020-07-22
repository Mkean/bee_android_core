package com.bee.update.impl;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;

import com.bee.core.logger.CommonLogger;
import com.bee.update.base.DownloadCallback;
import com.bee.update.base.DownloadNotifier;
import com.bee.update.model.Update;
import com.bee.update.util.ActivityManager;
import com.bee.update.util.SafeDialogHandle;

import java.io.File;

/**
 * 默认使用的下载进度创建通知器：在此创建Dialog弹窗显示并根据下载回调通知进行进度条更新
 */
public class DefaultDownloadNotifier extends DownloadNotifier {
    private static final String TAG = "Update";

    @Override
    public DownloadCallback create(Update update, Activity activity) {
        final ProgressDialog dialog = new ProgressDialog(activity);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setMax(100);
        dialog.setProgress(0);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        SafeDialogHandle.safeShowDialog(dialog);
        return new DownloadCallback() {
            @Override
            public void onDownloadStart() {
                CommonLogger.d(TAG, "开始下载");
            }

            @Override
            public void onDownloadComplete(File file) {
                SafeDialogHandle.safeDismissDialog(dialog);
                CommonLogger.d(TAG, "下载完成" + file.getAbsolutePath());

            }

            @Override
            public void onDownloadProgress(long current, long total) {
                int percent = (int) (current * 1.0f / total * 100);
                dialog.setProgress(percent);
                CommonLogger.d(TAG, "下载中..." + percent);

            }

            @Override
            public void onDownloadError(Throwable t) {
                SafeDialogHandle.safeDismissDialog(dialog);
                CommonLogger.d(TAG, "下载失败");
                createRestartDialog();
            }
        };
    }

    private void createRestartDialog() {
        CommonLogger.e(TAG, "创建重新下载Dialog");
        android.app.AlertDialog.Builder builder = new AlertDialog.Builder(ActivityManager.get().topActivity())
                .setCancelable(!update.isForced())
                .setTitle("下载apk失败")
                .setMessage("是否重新下载？")
                .setNeutralButton(update.isForced() ? "退出" : "取消", (dialog, which) -> {
                    if (update.isForced()) {
                        ActivityManager.get().exit();
                    } else {
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("确定", (dialog, which) -> restartDownload());

        builder.show();
    }
}
