package com.bee.update;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.net.ConnectivityManager;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.bee.android.common.app.CommonApplication;
import com.bee.android.common.bean.UpdateBean;
import com.bee.android.common.dialog.BaseDialog;
import com.bee.android.common.dialog.UpdateApkDialog;
import com.bee.android.common.event.UpdateSuggestAuthorizeEvent;
import com.bee.android.common.logger.CommonLogger;
import com.bee.android.common.manager.CommonFileManager;
import com.bee.android.common.permission.PermissionUtils;
import com.bee.android.common.permission.config.PermissionStr;
import com.bee.android.common.utils.AppUtils;
import com.bee.android.common.utils.CommonUtil;
import com.bee.android.common.utils.FileUtil;
import com.bee.android.common.utils.StringUtils;
import com.bee.android.common.utils.ToastUtils;
import com.bee.update.base.DownloadCallback;
import com.bee.update.model.Update;
import com.bee.update.util.ActivityManager;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.util.List;


/**
 * @Description: 版本更新工具类
 * <p>
 * TODO：记得修改下载路径，修改多次通知栏声音问题
 */
public class UpdateDownLoadUtils {

    private final String TAG = this.getClass().getSimpleName();

    private UpdateDownLoadUtils() {
    }

    public BaseDialog mNotificationDialog;

    int preCommons;

    public static UpdateDownLoadUtils getInstance() {
        return LazyHolder.INSTANCE;
    }

    private static class LazyHolder {
        private static UpdateDownLoadUtils INSTANCE = new UpdateDownLoadUtils();
    }

    public interface UpdateListener {
        void next();
    }

    public interface ClickListener {
        void cancel();

        void ensure();
    }

    @SuppressLint("SetTextI18n")
    public void startDownload(final UpdateBean bean, @NonNull UpdateApkDialog dialog,
                              @NonNull FragmentActivity activity, int type, final UpdateBuilder builder,
                              int i, final UpdateListener listener, @Nullable ClickListener clickListener) {

        this.preCommons = i; // 进度值为0 供其他页面重新计数
        if (bean == null)
            return;
        if (bean.getUpdate_type() != 3) {
            dialog.show();
        }
        // 设置版本号
        if (dialog.getTvVersionName() != null) {
            dialog.getTvVersionName().setText(StringUtils.getNumberToDINTypeface(activity, "版本号：V" + bean.getVersion()));
        }
        // 设置包大小
        if (dialog.getTvApkSize() != null && !TextUtils.isEmpty(bean.getFile_size())) {
            dialog.getTvApkSize().setText(StringUtils.getNumberToDINTypeface(activity, "大    小：" + bean.getFile_size() + "M"));
        }
        // 设置更新描述
        if (dialog.getDialogContentTv() != null) {
            dialog.getDialogContentTv().setText(TextUtils.isEmpty(bean.getDesc()) ? "" : bean.getDesc());
        }

        if (dialog.getTvUpdateDownloadError() != null && dialog.getTvStatusNet() != null) {
            if (checkCurrentVersionExist(bean.getVersion())) {
                dialog.getTvUpdateDownloadError().setText("免流量更新");
                dialog.getTvStatusNet().setText("已为您准备好最新版本的蜜蜂");
            } else {
                // 设置网络状态
                if (type == ConnectivityManager.TYPE_WIFI) {
                    dialog.getTvStatusNet().setText("当前处于wifi环境，更新不消耗流量");
                } else if (type == ConnectivityManager.TYPE_MOBILE) {
                    dialog.getTvStatusNet().setText("当前处于移动网络，下载会耗费流量");
                }
            }
        }

        // 设置升级按钮
        if (dialog.getLlNext() != null) {
            if (bean.getUpdate_type() == 1) { // 强制更新
                dialog.getLlNext().setVisibility(View.GONE);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) dialog.getLlNow().getLayoutParams();
                params.setMargins(0, 0, 0, 0);
                dialog.getLlNow().setLayoutParams(params);
            } else if (bean.getUpdate_type() == 2) { // 建议更新
                dialog.getLlNext().setOnClickListener(v -> {
                    CommonLogger.i(TAG, "点击下次提醒");
                    if (CommonUtil.isFastClick()) {
                        return;
                    }

                    dialog.dismiss();
                    if (listener != null) {
                        listener.next();
                    }
                    if (clickListener != null) {
                        clickListener.cancel();
                    }
                });
            }
        }

        if (dialog.getLlNow() != null) {
            dialog.getLlNow().setEnabled(true);
            dialog.getLlNow().setOnClickListener(v -> {
                if (CommonUtil.isFastClick()) {
                    return;
                }
                if (clickListener != null) {
                    clickListener.ensure();
                }
                PermissionUtils.requestPermission(activity, new PermissionUtils.RequestPermission() {
                    @Override
                    public void onRequestPermissionSuccess() {
                        // 判断存储是否小于150M
                        long availableInternalMemory = FileUtil.getAvailableExternalMemorySize() / 1024 / 1024;
                        if (availableInternalMemory < 150) {
                            ToastUtils.show("手机剩余存储空间不足，无法下载");
                        } else {
                            if ("免流量更新".equals(dialog.getTvUpdateDownloadError().getText().toString())) { // 免流量更新 点击直接安装
                                UpdateBuilder builder = UpdateBuilder.create();
                                File path = new File(CommonFileManager.getExternalFilePath(CommonApplication.app, "updatePlugin"));
                                File file = new File(path, "UpdatePlugin_" + bean.getVersion());
                                builder.getInstallStrategy().install(ActivityManager.get().getApplicationContext(), file.getAbsolutePath(), null);

                                if (bean.getUpdate_type() == 2) { // 建议更新
                                    dialog.dismiss();
                                    if (listener != null) {
                                        listener.next();
                                    }
                                }
                            } else {
                                if ("马上安装".equals(dialog.getTvUpdateDownloadError().getText().toString())) {
                                    UpdateBuilder builder = UpdateBuilder.create();
                                    File path = new File(CommonFileManager.getExternalFilePath(CommonApplication.app, "updatePlugin"));
                                    File file = new File(path, "UpdatePlugin_" + bean.getVersion());
                                    builder.getInstallStrategy().install(ActivityManager.get().getApplicationContext(), file.getAbsolutePath(), null);
                                } else {
                                    dialog.getLlNow().setEnabled(false);
                                    Update update = new Update();
                                    // 此apk包的下载地址
                                    update.setUpdateUrl(TextUtils.isEmpty(bean.getFile_url()) ? "" : bean.getFile_url());
                                    // 此apk包的版本名称
                                    update.setVersionName(bean.getVersion());
                                    // 此apk包的更新内容
                                    update.setUpdateContent(bean.getDesc());
                                    // 此apk包是否为强制更新
                                    if (bean.getUpdate_type() == 1) { // 强制更新
                                        update.setForced(true);
                                    } else if (bean.getUpdate_type() == 2) { // 建议更新
                                        update.setForced(false);
                                        EventBus.getDefault().post(new UpdateSuggestAuthorizeEvent());
                                    }

                                    builder.setDownloadCallback(new DownloadCallback() {
                                        @Override
                                        public void onDownloadStart() {
                                            CommonLogger.d(TAG, "download start");
                                            if (bean.getUpdate_type() == 2) { // 建议更新
                                                dialog.dismiss();
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                    // TODO: 通知栏 未完成
                                                }
                                            }
                                        }

                                        @Override
                                        public void onDownloadComplete(File file) {

                                        }

                                        @Override
                                        public void onDownloadProgress(long current, long total) {

                                        }

                                        @Override
                                        public void onDownloadError(Throwable t) {

                                        }
                                    });
                                }
                            }
                        }
                    }

                    @Override
                    public void onRequestPermissionFailure(List<String> permissionDeny, List<String> permissionNoAsk) {

                    }
                }, PermissionStr.WRITE_EXTERNAL_STORAGE, PermissionStr.READ_EXTERNAL_STORAGE);
            });
        }


    }

    public String getPermissionText(List<String> permissions) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String permission : permissions) {
            if (Manifest.permission.CAMERA.equals(permission)) {
                stringBuilder.append("、相机");
            } else if (Manifest.permission.WRITE_EXTERNAL_STORAGE.equals(permission) || Manifest.permission.READ_EXTERNAL_STORAGE.equals(permission)) {
                int indexOf = stringBuilder.indexOf("存储");
                if (indexOf == -1) {
                    stringBuilder.append("、存储");
                }
            } else if (Manifest.permission.RECORD_AUDIO.equals(permission)) {
                stringBuilder.append("、录音");
            }
        }
        if (stringBuilder.toString().length() > 0) {
            return stringBuilder.toString().substring(1);
        } else {
            return "";
        }
    }

    /**
     * 检测当前版本是否已存在
     *
     * @param version
     * @return
     */
    private boolean checkCurrentVersionExist(String version) {
        boolean b = false;
        try {
            File totalLengthFile = new File(CommonFileManager.getExternalFilePath(CommonApplication.app, "updatePlugin"), "totalLengthFile");
            if (totalLengthFile.exists() && totalLengthFile.length() > 0) {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(totalLengthFile));
                String length = bufferedReader.readLine();
                CommonLogger.d(TAG, "apk文件的总大小 ： " + length);
                String versionName = AppUtils.getVersionName(CommonApplication.app);
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
                                if (downLoadApk.exists() && downLoadApk.length() == Long.parseLong(length) && !TextUtils.isEmpty(split[1]) && CommonUtil.compareVersion(version, split[1]) == 0 && CommonUtil.compareVersion(version, versionName) == 1) {
                                    b = true;
                                }
                            }
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            CommonLogger.printErrStackTrace(TAG, e, "");
        }
        return b;
    }

}
