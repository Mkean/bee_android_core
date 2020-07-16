package com.bee.update.base;

import android.app.Activity;
import android.app.Dialog;

import com.bee.update.UpdateBuilder;
import com.bee.update.flow.Launcher;
import com.bee.update.model.Update;
import com.bee.update.util.ActivityManager;
import com.bee.update.util.UpdatePreference;

/**
 * 此为当检查到更新时的通知创建器基类
 */
public abstract class CheckNotifier {

    protected UpdateBuilder builder;
    protected Update update;
    private CheckCallback callback;

    public final void setBuilder(UpdateBuilder builder) {
        this.builder = builder;
        this.callback = builder.getCheckCallback();
    }

    public void setUpdate(Update update) {
        this.update = update;
    }

    /**
     * 创建一个Dialog用于通知用户当前有新版本需要更新。
     *
     * <P>若需要展示的通知为非弹窗通知。如在通知栏进行通知，则再次创建通知栏通知，返回为null即可
     *
     * <p>定制说明：</p>
     * 1.当需要进行后续更新操作时（请求进行apk下载任务）：调用{@link #sendDownloadRequest()}
     * 2.当需要取消此次更新操作时：调用{@link #sendUserCancel()}
     * 3.当需要忽略此版本更新时：调用{@link #sendUserIgnore()}
     *
     * @param activity 顶部的Activity实例，通过{@link ActivityManager#topActivity};
     * @return 创建的Dialog实例。若当需要展示的不是弹窗时：返回null
     */
    public abstract Dialog create(Activity activity);

    /**
     * 当需要进行后续更新操作时：需要更新，启动下载任务时，调用此方法进行流程连接
     */
    public final void sendDownloadRequest() {
        Launcher.getInstance().launchDownload(update,builder);
    }

    /**
     * 当用户手动取消此次更新任务时，通过此方法进行取消并通知用户
     */
    protected final void sendUserCancel() {
        if (this.callback != null) {
            this.callback.onUserCancel();
        }
    }

    /**
     * 当用户指定需要忽略此版本的更新请求时：通过此方法进行取消并忽略此版本的后续更新请求。
     */
    protected final void sendUserIgnore() {
        if (this.callback != null) {
            this.callback.onCheckIgnore(update);
        }
        UpdatePreference.saveIgnoreVersion(update.getVersionCode());
    }
}
