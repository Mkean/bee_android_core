package com.bee.update.flow;

import android.app.Activity;
import android.app.Dialog;

import com.bee.update.UpdateBuilder;
import com.bee.update.base.CheckCallback;
import com.bee.update.base.CheckNotifier;
import com.bee.update.base.CheckWorker;
import com.bee.update.model.Update;
import com.bee.update.util.ActivityManager;
import com.bee.update.util.SafeDialogHandle;
import com.bee.update.util.Utils;

/**
 * @Description: 核心操作类
 * <p>
 * 此类为默认的检查更新api网络任务的通知回调，用于接收从{@link CheckWorker} 中所传递过来的各种状态，并进行后续流程触发
 */
public class DefaultCheckCallback implements CheckCallback {

    private UpdateBuilder builder;
    private CheckCallback callback;

    public DefaultCheckCallback(UpdateBuilder builder) {
        this.builder = builder;
        this.callback = builder.getCheckCallback();
    }

    @Override
    public void onCheckStart() {
        try {
            if (callback != null) {
                callback.onCheckStart();
            }
        } catch (Throwable t) {
            onCheckError(t);
        }
    }

    @Override
    public void hasUpdate(Update update) {
        try {
            if (callback != null) {
                callback.hasUpdate(update);
            }

            CheckNotifier notifier = builder.getCheckNotifier();
            notifier.setBuilder(builder);
            notifier.setUpdate(update);
            Activity current = ActivityManager.get().topActivity();

            if (Utils.isValid(current)
                    && builder.getUpdateStrategy().isShowUpdateDialog(update)) {
                Dialog dialog = notifier.create(current);
                SafeDialogHandle.safeShowDialog(dialog);
            } else {
                notifier.sendDownloadRequest();
            }

        } catch (Throwable t) {
            onCheckError(t);
        }
    }

    @Override
    public void noUpdate() {
        try {
            if (callback != null) {
                callback.noUpdate();
            }
        } catch (Throwable t) {
            onCheckError(t);
        }
    }

    @Override
    public void onCheckError(Throwable t) {
        try {
            if (callback != null) {
                callback.onCheckError(t);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUserCancel() {
        try {
            if (callback != null) {
                callback.onUserCancel();
            }
        } catch (Throwable t) {
            onCheckError(t);
        }
    }

    @Override
    public void onCheckIgnore(Update update) {
        try {
            if (callback != null) {
                callback.onCheckIgnore(update);
            }
        } catch (Throwable t) {
            onCheckError(t);
        }
    }
}
