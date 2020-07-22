package com.bee.update.impl;

import android.app.Activity;
import android.app.Dialog;

import androidx.appcompat.app.AlertDialog;

import com.bee.core.logger.CommonLogger;
import com.bee.update.base.CheckNotifier;
import com.bee.update.util.SafeDialogHandle;

/**
 * 默认使用的在检查到有更新版本时的通知创建器：创建一个弹窗提示用户当前有新版本需要更新。
 */
public class DefaultUpdateNotifier extends CheckNotifier {

    public static final String TAG = "Update";

    @Override
    public Dialog create(Activity activity) {
        String updateContent = "版本号：" + update.getVersionName() + "\n\n\n"
                + update.getUpdateContent();
        CommonLogger.d(TAG, updateContent);

        AlertDialog.Builder builder = new AlertDialog.Builder(activity)
                .setTitle("你有新版本需要更新")
                .setMessage(updateContent)
                .setPositiveButton("立即更新", (dialog, which) -> {
                    sendDownloadRequest();
                    SafeDialogHandle.safeDismissDialog((Dialog) dialog);
                });
        if (update.isIgnore() && !update.isForced()) {
            builder.setNeutralButton("忽略此版本", (dialog, which) -> {
                sendUserIgnore();
                SafeDialogHandle.safeDismissDialog((Dialog) dialog);
            });
        }

        if (!update.isForced()) {
            builder.setNegativeButton("取消", (dialog, which) -> {
                sendUserCancel();
                SafeDialogHandle.safeDismissDialog((Dialog) dialog);
            });
        }

        builder.setCancelable(false);
        return builder.create();
    }
}
