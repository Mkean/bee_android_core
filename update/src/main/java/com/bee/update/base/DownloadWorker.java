package com.bee.update.base;

import android.text.TextUtils;

import com.bee.android.common.app.CommonApplication;
import com.bee.android.common.event.ApkInstallEvent;
import com.bee.android.common.logger.CommonLogger;
import com.bee.android.common.manager.CommonFileManager;
import com.bee.android.common.utils.AppUtils;
import com.bee.android.common.utils.CommonUtil;
import com.bee.android.common.utils.FileUtil;
import com.bee.update.UpdateBuilder;
import com.bee.update.flow.DefaultDownloadCallback;
import com.bee.update.impl.DefaultDownloadWorker;
import com.bee.update.model.Update;
import com.bee.update.util.Utils;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * <b>核心操作类</b>
 * <p>
 * 此为下载任务的封装基类，主要用于对下载中的进度、状态进行派发，以起到连接更新流程的作用
 */
public abstract class DownloadWorker implements Runnable {


    private final String TAG = "Update";

    /**
     * {@link DefaultDownloadCallback}的实例。用于接收下载状态并进行后续流程通知
     */
    private DefaultDownloadCallback callback;

    // 维护一个所有下载任务的下载文件集合。用于避免出现多个下载任务同时下载同一个文件。避免冲突。
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
    public final void run() {
        try {
            File file = builder.getFileCreator().createWithBuilder(update, builder);
            FileChecker checker = builder.getFileChecker();
            CommonLogger.d(TAG, "开始下载文件将要存储的路径 ： " + file.getAbsolutePath());
            checker.attach(update, file);
            File totalLengthFile = new File(CommonFileManager.getExternalFilePath(CommonApplication.app, "updatePlugin"), "totalLengthFile");
            if (totalLengthFile.exists() && totalLengthFile.length() > 0) {

                BufferedReader bufferedReader = new BufferedReader(new FileReader(totalLengthFile));
                String length = bufferedReader.readLine();
                CommonLogger.d(TAG, "已下载apk 文件的大小 ： " + file.length());
                CommonLogger.d(TAG, "apk文件的总大小 ： " + length);


                String versionName = AppUtils.getVersionName(CommonApplication.app);

//                if (builder.getFileChecker().checkBeforeDownload()&&file.length()==Long.parseLong(length)&& CommonUtil.compareVersion(update.getVersionName(),versionName)==1) {
                // check success: skip download and show install dialog if needed.
//                    callback.postForInstall(file);
                File path = new File(CommonFileManager.getExternalFilePath(CommonApplication.app, "updatePlugin"));
                String[] strings = path.list();
                if (strings != null && strings.length > 0) {
                    CommonLogger.d(TAG, "获取已下载apk的updatePlugin" + Arrays.toString(strings));

                    for (int i = 0; i < strings.length; i++) {
                        if (strings[i].contains("UpdatePlugin")) {

                            String[] split = strings[i].split("_");

                            if (split != null && split.length == 2) {
                                CommonLogger.d(TAG, "获取已下载apk的版本号" + Arrays.toString(split));
                                File downLoadApk = new File(path, strings[i]);

                                if (downLoadApk.exists() && downLoadApk.length() == Long.parseLong(length) && !TextUtils.isEmpty(split[1]) && CommonUtil.compareVersion(update.getVersionName(), split[1]) == 0 && CommonUtil.compareVersion(update.getVersionName(), versionName) == 1) {

                                    EventBus.getDefault().post(new ApkInstallEvent());
                                    return;
                                }

                            }
                            break;
                        }
                    }
                }
//                }
            }

            checkDuplicateDownload(file);
            //此处判断本地完全下载完成 && 最新版本 &&运行版本比此版本低  则进行免流量更新

            File path = new File(CommonFileManager.getExternalFilePath(CommonApplication.app, "updatePlugin"));
            String[] strings = path.list();
            if (strings != null && strings.length > 0) {
                for (int i = 0; i < strings.length; i++) {
                    CommonLogger.d(TAG, "获取已下载apk的updatePlugin11111" + Arrays.toString(strings));

                    if (strings[i].contains("UpdatePlugin")) {

                        String[] split = strings[i].split("_");

                        if (split != null && split.length > 1) {
                            CommonLogger.d(TAG, "获取已下载apk的版本号" + Arrays.toString(split));

                            if (!TextUtils.isEmpty(split[1]) && CommonUtil.compareVersion(update.getVersionName(), split[1]) == 1) {

                                try {
                                    FileUtil.deleteFile(path);
                                    CommonLogger.d(TAG, "删除apk下载路径全部文件成功");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    CommonLogger.d(TAG, "删除apk下载路径全部文件失败");

                                }
                            }

                        }
                        break;
                    }
                }
            }


            sendDownloadStart();
            String url = update.getUpdateUrl();
            file.getParentFile().mkdirs();

            download(url, file);
        } catch (Throwable e) {

            sendDownloadError(e);
        }
    }

    private void checkDuplicateDownload(File file) {
        if (downloading.containsValue(file)) {
            throw new RuntimeException(String.format(
                    "You can not download the same file using multiple download tasks simultaneously，the file path is %s",
                    file.getAbsolutePath()
            ));
        }
        downloading.put(this, file);
    }

    /**
     * 此为更新插件apk下载任务触发入口。
     *
     * <p>定制自己的网络层下载任务。需要复写此方法。并执行网络下载任务。
     *
     * <p>定制网络下载任务需要自己进行状态通知：
     * <ol>
     * <li>当使用的网络框架可以支持进度条通知时，调用{@link #sendDownloadProgress(long, long)}触发进度条消息通知</li>
     * <li>当下载出现异常时。若使用的是同步请求。则无需理会，若使用的是异步请求。则需手动调用{@link #sendDownloadError(Throwable)}</li>
     * <li>当下载任务执行完毕时：需手动调用{@link #sendDownloadComplete(File)}通知用户并启动下一步任务</li>
     * </ol>
     *
     * @param url    apk文件下载地址
     * @param target 指定的下载文件地址
     * @throws Exception 捕获出现的异常
     */
    protected abstract void download(String url, File target) throws Exception;

    protected final void sendDownloadStart() {
        if (callback == null) return;

        Utils.getMainHandler().post(() -> {
            if (callback == null) return;
            callback.onDownloadStart();
        });
    }

    /**
     * 通知当前下载进度，若实现类不调用此方法。将不会触发更新进度条的消息
     *
     * @param current 当前下载长度e
     * @param total   下载文件总长度
     */
    protected final void sendDownloadProgress(final long current, final long total) {
        if (callback == null) return;

        Utils.getMainHandler().post(() -> {
            if (callback == null) return;
            callback.onDownloadProgress(current, total);
        });
    }

    /**
     * 通知当前下载任务执行完毕！当下载完成后。此回调必须被调用
     *
     * @param file 被下载的文件
     */
    protected final void sendDownloadComplete(final File file) {
        try {
            builder.getFileChecker().onCheckBeforeInstall();
        } catch (Exception e) {
            sendDownloadError(e);
            return;
        }

        if (callback == null) return;
        Utils.getMainHandler().post(() -> {
            if (callback == null) return;
            callback.onDownloadComplete(file);
            callback.postForInstall(file);
            downloading.remove(DownloadWorker.this);
        });
    }

    /**
     * 通知当前下载任务出错。当下载出错时，此回调必须被调用
     *
     * @param t 错误异常信息
     */
    protected final void sendDownloadError(final Throwable t) {
        if (callback == null) return;

        Utils.getMainHandler().post(() -> {
            if (callback == null) return;
            callback.onDownloadError(t);
            downloading.remove(DownloadWorker.this);
        });
    }
}
