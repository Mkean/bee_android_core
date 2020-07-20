package com.bee.update.base;

import android.app.Activity;
import android.app.Dialog;

import com.bee.android.common.logger.CommonLogger;
import com.bee.update.UpdateBuilder;
import com.bee.update.UpdateConfig;
import com.bee.update.impl.DefaultInstallNotifier;
import com.bee.update.model.Update;
import com.bee.update.util.ActivityManager;
import com.bee.update.util.UpdatePreference;

import java.io.File;

/**
 * 此类为当前检查到更新时的通知创建器基类。
 * <p>
 * 配置方式：{@link UpdateConfig#setInstallNotifier(InstallNotifier)}或者{@link UpdateBuilder#setInstallNotifier(InstallNotifier)}
 * <p>
 * 默认实现：{@link DefaultInstallNotifier}
 * <p>
 * 触发逻辑：当检查到有新版本更新时且配置的更新策略{@link UpdateStrategy#isAutoInstall()}为false时，此通知创建器将被触发。
 *
 * <ol> 定制说明:
 *     <li>当需要进行后续更新操作时（请求调起安装任务），调用{@link #sendToInstall()}</li>
 *     <li>当需要取消此次更新操作时，调用{@link #sendUserCancel()}</li>
 *     <li>当需要忽略此版本更新时，调用{@link #sendCheckIgnore()}</li>
 * </ol>
 */
public abstract class InstallNotifier {
    private static final String TAG = "Update";
    protected UpdateBuilder builder;
    protected Update update;
    protected File file;

    public final void setBuilder(UpdateBuilder builder) {
        this.builder = builder;
    }

    public final void setUpdate(Update update) {
        this.update = update;
    }

    public final void setFile(File file) {
        this.file = file;
    }

    /**
     * 创建一个Dialog进行界面展示。提示用户当前有新版本可更新。且当前apk已下载完成。
     *
     * <p>数据来源：获取父类的{@link #update}属性
     *
     * <p>定制说明：<br>
     * 1. 当需要进行后续更新操作时(请求调起安装任务)：调用{@link #sendToInstall()}}<br>
     * 2. 当需要取消此次更新操作时：调用{@link #sendUserCancel()}<br>
     * 3. 当需要忽略此版本更新时：调用{@link #sendCheckIgnore()}<br>
     *
     * @param activity 当前最顶层的Activity实例。用于提供界面展示功能
     * @return 需要展示的Dialog。若为null则表示不进行Dialog展示。比如说此处需要以Notification样式进行展示时。
     */
    public abstract Dialog create(Activity activity);

    /**
     * 请求调起安装任务
     */
    public final void sendToInstall() {
        CommonLogger.d(TAG, "请求调起安装任务");
        builder.getInstallStrategy().install(ActivityManager.get().getApplicationContext(), file.getAbsolutePath(), update);
    }

    /**
     * 请求取消更新任务
     */
    public final void sendUserCancel() {
        if (builder.getCheckCallback() != null) {
            CommonLogger.d(TAG, "请求取消更新任务");
            builder.getCheckCallback().onUserCancel();
        }

    }

    /**
     * 请求将此版本加入版本忽略列表
     */
    public final void sendCheckIgnore() {
        if (builder.getCheckCallback() != null) {
            builder.getCheckCallback().onCheckIgnore(update);
            CommonLogger.d(TAG, "请求将此版本加入版本忽略列表");
        }
        UpdatePreference.saveIgnoreVersion(update.getVersionCode());
    }
}
