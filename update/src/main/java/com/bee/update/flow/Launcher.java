package com.bee.update.flow;

import com.bee.core.logger.CommonLogger;
import com.bee.update.UpdateBuilder;
import com.bee.update.base.CheckWorker;
import com.bee.update.base.DownloadWorker;
import com.bee.update.model.Update;

/**
 * 此类用于调起更新流程中的两处网络任务进行执行。
 *
 * <p> 1. 检查更新api网络任务{@link #launchCheck(UpdateBuilder)}
 *
 * <p> 2.发起apk文件下载任务:{@link #launchDownload(Update, UpdateBuilder)}
 */
public class Launcher {
    public static final String TAG = "Update";

    private static Launcher launcher;

    private Launcher() {
    }

    public static Launcher getInstance() {
        if (launcher == null) {
            launcher = new Launcher();
        }
        return launcher;
    }

    /**
     * 调起检查api更新任务
     *
     * @param builder 更新任务实例
     */
    public void launchCheck(UpdateBuilder builder) {
        // 定义一个默认的检查更新回调监听，用于接收api检查更新任务所发出的通知，并连接后续流程
        CommonLogger.d(TAG, "调起检查api更新任务");
        DefaultCheckCallback checkCB = new DefaultCheckCallback();
        checkCB.setBuilder(builder);
        checkCB.onCheckStart();
        CheckWorker checkWorker = null;

        try {
            checkWorker = builder.getCheckWorker().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(String.format(
                    "Could not create instance for %s", builder.getCheckWorker().getCanonicalName()), e);
        }
        checkWorker.setBuilder(builder);
        checkWorker.setCheckCB(checkCB);
        builder.getConfig().getExecutor().execute(checkWorker);

    }

    /**
     * 调起apk文件下载任务
     *
     * @param update  更新api实体类。不能为null
     * @param builder 更新任务实例
     */
    public void launchDownload(Update update, UpdateBuilder builder) {
        // 定义一个默认的下载状态回调监听，用于接收文件下载任务所发出的通知，并连接后续流程
        CommonLogger.d(TAG, "调起apk下载任务");
        DefaultDownloadCallback downloadCB = new DefaultDownloadCallback();
        downloadCB.setBuilder(builder);
        downloadCB.setUpdate(update);
        downloadCB.onDownloadStart();

        DownloadWorker downloadWorker = null;

        try {
            downloadWorker = builder.getDownloadWorker().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(String.format(
                    "Could not create instance for %s", builder.getDownloadWorker().getCanonicalName()
            ), e);
        }
        downloadWorker.setUpdate(update);
        downloadWorker.setUpdateBuilder(builder);
        downloadWorker.setCallback(downloadCB);

        builder.getConfig().getExecutor().execute(downloadWorker);
    }
}
