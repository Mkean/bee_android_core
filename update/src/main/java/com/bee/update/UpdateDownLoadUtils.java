package com.bee.update;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
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
import com.bee.android.common.dialog.DialogManager;
import com.bee.android.common.dialog.UpdateApkDialog;
import com.bee.android.common.event.UpdateSuggestAuthorizeEvent;
import com.bee.android.common.logger.CommonLogger;
import com.bee.android.common.manager.CommonFileManager;
import com.bee.android.common.permission.PermissionUtils;
import com.bee.android.common.permission.config.PermissionStr;
import com.bee.android.common.utils.AppUtils;
import com.bee.android.common.utils.CommonUtil;
import com.bee.android.common.utils.FileUtil;
import com.bee.android.common.utils.NotificationsUtils;
import com.bee.android.common.utils.StringUtils;
import com.bee.android.common.utils.ToastUtils;
import com.bee.update.base.DownloadCallback;
import com.bee.update.flow.Launcher;
import com.bee.update.impl.DefaultDownloadWorker;
import com.bee.update.impl.DefaultInstallStrategy;
import com.bee.update.model.Update;
import com.bee.update.update.AllDialogShowStrategy;
import com.bee.update.update.CustomApkFileCreator;
import com.bee.update.update.NotificationDownloadCreator;
import com.bee.update.update.NotificationDownloadErrorCreator;
import com.bee.update.update.NotificationInstallCreator;
import com.bee.update.util.ActivityManager;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.http.Url;


/**
 * @Description: 版本更新工具类
 * <p>
 * TODO：记得修改下载路径，修改多次通知栏声音问题
 */
public class UpdateDownLoadUtils {

    private UpdateDownLoadUtils() {
    }

    public BaseDialog mNotificationDialog;

    int preProgress;
    private final String TAG = this.getClass().getSimpleName();

    public static UpdateDownLoadUtils getInstance() {
        return UpdateDownLoadUtils.LazyHolder.INSTANCE;
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
    public void startDownLoad(final UpdateBean bean, @NonNull final UpdateApkDialog dialog,
                              @NonNull FragmentActivity activity, int type, final UpdateBuilder mUpdateBuilder,
                              int i, final UpdateListener mUpdateListener, @Nullable ClickListener mClickListener) {
        this.preProgress = i;//进度置为0 供其他页面重新计数
        if (bean == null) {
            return;
        }
        if (bean.getUpdate_type() != 3) {
            dialog.show();

            //设置版本号
            if (dialog != null && dialog.getTvVersionName() != null) {
                dialog.getTvVersionName().setText(StringUtils.getNumberToDINTypeface(activity, "版本号：V" + bean.getVersion()));
            }

            //设置包大小
            if (dialog.getTvApkSize() != null && !TextUtils.isEmpty(bean.getFile_size())) {
                dialog.getTvApkSize().setText(StringUtils.getNumberToDINTypeface(activity, "大    小：" + bean.getFile_size() + "M"));
            }

            //设置更新描述
            if (dialog.getDialogContentTv() != null) {
                dialog.getDialogContentTv().setText(TextUtils.isEmpty(bean.getDesc()) ? "" : bean.getDesc());
            }

            if (checkCurrentVersionExist(bean.getVersion())) {
                dialog.getTvUpdateDownloadError().setText("免流量更新");
                dialog.getTvStatusNet().setText("已为您准备好最新版本的小猴AI课");
            } else {
                //设置网络状态
                if (dialog.getTvStatusNet() != null) {
                    if (type == ConnectivityManager.TYPE_WIFI) {
                        dialog.getTvStatusNet().setText("当前处于wifi环境，更新不消耗流量");
                    } else if (type == ConnectivityManager.TYPE_MOBILE) {
                        dialog.getTvStatusNet().setText("当前处于移动网络，下载会耗费流量");
                    }
                }
            }

            //设置升级按钮
            if (dialog.getLlNext() != null) {
                if (bean.getUpdate_type() == 1) {//强制更新
                    dialog.getLlNext().setVisibility(View.GONE);
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) dialog.getLlNow().getLayoutParams();
                    params.setMargins(0, 0, 0, 0);
                    dialog.getLlNow().setLayoutParams(params);
                } else if (bean.getUpdate_type() == 2) {//建议更新
                    dialog.getLlNext().setOnClickListener(v -> {
                        CommonLogger.i(TAG, "点击下次提醒");
                        if (CommonUtil.isFastClick()) {
                            return;
                        }
                        dialog.dismiss();
                        if (mUpdateListener != null) {
                            mUpdateListener.next();
                        }
                        if (mClickListener != null) {
                            mClickListener.cancel();
                        }
                    });
                }
            }

          /*  if (bean.getLocal() == 1) {//已下载到本地 免流量更新
                mUpdateApkDialog.tv_update_download_error.setText("免流量更新");
            }*/
            if (dialog.getLlNow() == null) return;
            dialog.getLlNow().setEnabled(true);

            dialog.getLlNow().setOnClickListener(v -> {
                if (CommonUtil.isFastClick()) {
                    return;
                }
                //迁移注释 和敬旺沟通可以注释
                //activity.showLoading();
                if (mClickListener != null) {
                    mClickListener.ensure();
                }

                PermissionUtils.requestPermission(activity, new PermissionUtils.RequestPermission() {
                    @Override
                    public void onRequestPermissionSuccess() {
                        //判断存储是否小于150M
                        long availableInternalMemory = FileUtil.getAvailableExternalMemorySize() / 1024 / 1024;
                        if (availableInternalMemory < 150) {
                            //迁移注释 和敬旺沟通可以注释
                            //activity.dismissLoading();
                            ToastUtils.show("手机剩余存储空间不足，无法下载");
                        } else {
                            if ("免流量更新".equals(dialog.getTvUpdateDownloadError().getText().toString())) {//免流量更新 点击直接安装
                                UpdateBuilder builder = UpdateBuilder.create();
                                File path = new File(CommonFileManager.getExternalFilePath(CommonApplication.app, "updatePlugin"));
                                File file = new File(path, "UpdatePlugin_" + bean.getVersion());
                                builder.getInstallStrategy().install(ActivityManager.get().getApplicationContext(), file.getAbsolutePath(), null);

                                //迁移注释 和敬旺沟通可以注释
                                //activity.dismissLoading();

                                if (bean.getUpdate_type() == 2) {//建议更新
                                    dialog.dismiss();
                                    if (mUpdateListener != null) {
                                        mUpdateListener.next();
                                    }
                                }
                            } else {
                                if ("马上安装".equals(dialog.getTvUpdateDownloadError().getText().toString())) {
                                    UpdateBuilder builder = UpdateBuilder.create();
                                    File path = new File(CommonFileManager.getExternalFilePath(CommonApplication.app, "updatePlugin"));
                                    File file = new File(path, "UpdatePlugin_" + bean.getVersion());
                                    builder.getInstallStrategy().install(ActivityManager.get().getApplicationContext(), file.getAbsolutePath(), null);
                                    //迁移注释 和敬旺沟通可以注释
                                    //activity.dismissLoading();
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
                                    if (bean.getUpdate_type() == 1) {//强制更新
                                        update.setForced(true);
                                    } else if (bean.getUpdate_type() == 2) {//建议更新
                                        update.setForced(false);
                                        EventBus.getDefault().post(new UpdateSuggestAuthorizeEvent());
                                    }

                                    mUpdateBuilder.setDownloadCallback(new DownloadCallback() {
                                        @Override
                                        public void onDownloadStart() {
                                            CommonLogger.d(TAG, "download start");
                                            //迁移注释 和敬旺沟通可以注释
                                            //activity.dismissLoading();
                                            if (bean.getUpdate_type() == 2) {//建议更新
                                                dialog.dismiss();
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                    if (!NotificationsUtils.isEnableV26(CommonApplication.app)) {
                                                        CommonLogger.i(TAG, "未开启通知栏权限");
                                                        if (mNotificationDialog == null) {
                                                            mNotificationDialog = DialogManager.getInstance().showTitleContentStrTwoButtonDialog(activity, "提示", "我们需要获得通知权限，才能为您提供提醒服务", "暂不开启", "去开启", new BaseDialog.ActionListener() {
                                                                @Override
                                                                public void clickLeft() {

                                                                }

                                                                @Override
                                                                public void clickRight() {
                                                                    Intent localIntent = new Intent();
                                                                    localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                                    if (Build.VERSION.SDK_INT >= 9) {
                                                                        localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                                                                        localIntent.setData(Uri.fromParts("package", activity.getPackageName(), null));
                                                                    } else if (Build.VERSION.SDK_INT <= 8) {
                                                                        localIntent.setAction(Intent.ACTION_VIEW);
                                                                        localIntent.setClassName("com.android.settings",
                                                                                "com.android.settings.InstalledAppDetails");
                                                                        localIntent.putExtra("com.android.settings.ApplicationPkgName",
                                                                                activity.getPackageName());
                                                                    }
                                                                    activity.startActivity(localIntent);
                                                                    if (mUpdateListener != null) {
                                                                        mUpdateListener.next();
                                                                    }
                                                                }
                                                            });

                                                        }
                                                    } else {
                                                        CommonLogger.d(TAG, "已开启通知栏权限");
                                                    }

                                                } else {
                                                    if (!NotificationsUtils.isNotificationEnabled(CommonApplication.app)) {
                                                        CommonLogger.d(TAG, "未开启通知栏权限");
                                                        if (mNotificationDialog == null) {
                                                            mNotificationDialog = DialogManager.getInstance().showTitleContentStrTwoButtonDialog(activity, "提示", "我们需要获得通知权限，才能为您提供提醒服务", "暂不开启", "去开启", new BaseDialog.ActionListener() {
                                                                @Override
                                                                public void clickLeft() {

                                                                }

                                                                @Override
                                                                public void clickRight() {
                                                                    Intent localIntent = new Intent();
                                                                    localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                                    if (Build.VERSION.SDK_INT >= 9) {
                                                                        localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                                                                        localIntent.setData(Uri.fromParts("package", activity.getPackageName(), null));
                                                                    } else if (Build.VERSION.SDK_INT <= 8) {
                                                                        localIntent.setAction(Intent.ACTION_VIEW);
                                                                        localIntent.setClassName("com.android.settings",
                                                                                "com.android.settings.InstalledAppDetails");
                                                                        localIntent.putExtra("com.android.settings.ApplicationPkgName",
                                                                                activity.getPackageName());
                                                                    }
                                                                    activity.startActivity(localIntent);

                                                                    if (mUpdateListener != null) {
                                                                        mUpdateListener.next();
                                                                    }
                                                                }
                                                            });

                                                        }
                                                    } else {
                                                        CommonLogger.d(TAG, "已开启通知栏权限");
                                                    }
                                                }
                                            }
                                        }

                                        @Override
                                        public void onDownloadComplete(File file) {
                                            dialog.getLlNow().setEnabled(true);

                                            CommonLogger.d(TAG, "download  Complete");
                                            if (bean.getUpdate_type() == 1) {
                                                dialog.getLlBtnTips().setVisibility(View.VISIBLE);

                                                dialog.getUpdateCornerRl().setVisibility(View.GONE);

                                                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) dialog.getLlNow().getLayoutParams();
                                                params.setMargins(0, 0, 0, 0);

                                                dialog.getLlNow().setLayoutParams(params);
                                                dialog.getTvStatusNet().setVisibility(View.VISIBLE);
                                                dialog.getTvStatusNet().setText("已为您准备好最新版本的小猴AI课");
                                                dialog.getTvUpdateDownloadError().setText("马上安装");
                                            }
                                        }

                                        @Override
                                        public void onDownloadProgress(long current, long total) {
                                            if (bean.getUpdate_type() == 1) {
                                                dialog.getLlBtnTips().setVisibility(View.GONE);

                                                dialog.getUpdateCornerRl().setVisibility(View.VISIBLE);
                                                //mUpdateApkDialog.tv_status_net.setVisibility(View.INVISIBLE);
                                                // 下载过程中的进度信息。在此获取进度信息。运行于主线程
                                                try {
                                                    if (total != dialog.getProgressBar().getMax()) {
                                                        CommonLogger.i(TAG, "下载总进度： " + total);
                                                        dialog.getProgressBar().setMax((int) total);
                                                    }
                                                    dialog.getProgressBar().setProgress((int) current);
                                                } catch (Exception e) {
                                                    CommonLogger.printErrStackTrace(TAG, e, "");
                                                }

                                                int progress = (int) (current * 100 / total);
                                                CommonLogger.d(TAG, "下载进度： " + progress);
                                                if (preProgress < progress) {
                                                    preProgress = progress;
                                                    // 过滤不必要的刷新进度
                                                    dialog.getTvProgress().setText(StringUtils.getNumberToDINTypeface(activity, progress + "%"));
                                                }
                                            }
                                        }

                                        @Override
                                        public void onDownloadError(Throwable t) {
                                            dialog.getLlNow().setEnabled(true);

                                            // 下载时出错。运行于主线程
                                            CommonLogger.d(TAG, "download  Error ");
                                            if (bean.getUpdate_type() == 1) {
                                                dialog.getLlBtnTips().setVisibility(View.VISIBLE);
                                                dialog.getLlNext().setVisibility(View.GONE);

                                                dialog.getUpdateCornerRl().setVisibility(View.GONE);

                                                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) dialog.getLlNow().getLayoutParams();
                                                params.setMargins(0, 0, 0, 0);

                                                dialog.getLlNow().setLayoutParams(params);
                                                dialog.getTvUpdateDownloadError().setText("下载失败，点击重试");
                                            }
                                            //迁移注释 和敬旺沟通可以注释
                                            //activity.dismissLoading();
                                        }
                                    });
                                    DefaultDownloadWorker.downloadFlag = true;
                                    mUpdateBuilder.setInstallNotifier(new NotificationInstallCreator());
                                    mUpdateBuilder.setFileCreator(new CustomApkFileCreator());
                                    mUpdateBuilder.setUpdateStrategy(new AllDialogShowStrategy());
                                    mUpdateBuilder.setDownloadNotifier(new NotificationDownloadCreator());
                                    mUpdateBuilder.setDownloadErrorNotifier(new NotificationDownloadErrorCreator());//下载失败点击重试通知
                                    Launcher.getInstance().launchDownload(update, mUpdateBuilder);
                                }
                            }
                        }
                    }

                    @Override
                    public void onRequestPermissionFailure(List<String> permissionDeny, List<String> permissionNoAsk) {
                        //迁移注释 和敬旺沟通可以注释
                        //activity.dismissLoading();

                        String content = getPermissionText(permissionDeny);

                        DialogManager.getInstance().showTitleContentTwoButtonDialog(activity, "权限被禁用", "请到\"设置-应用-小猴思维\"中打开" + content + "权限", "取消", "去设置", new BaseDialog.ActionListener() {
                            @Override
                            public void clickLeft() {
                            }

                            @Override
                            public void clickRight() {
                                Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
                                intent.setData(Uri.parse("package:" + CommonApplication.app.getPackageName()));
                                CommonApplication.app.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                            }
                        });
                    }
                }, PermissionStr.WRITE_EXTERNAL_STORAGE, PermissionStr.READ_EXTERNAL_STORAGE);
            });
        } else {
            if (mUpdateListener != null) {
                mUpdateListener.next();
            }
            if (mClickListener != null) {
                mClickListener.cancel();
            }
            //判断本地apk是否已完全下载到本地
            Observable.create((ObservableOnSubscribe<Boolean>) e -> {

                CommonLogger.i(TAG, "权限开启的情况下 删除下载的apk文件");
                File path = new File(CommonFileManager.getExternalFilePath(CommonApplication.app, "updatePlugin"));
                try {
                    FileUtil.deleteFile(path);
                    e.onNext(true);
                    e.onComplete();
                } catch (Exception exception) {
                    e.onError(exception);
                }
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Boolean>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(Boolean aBoolean) {
                            CommonLogger.i(TAG, "删除apk下载路径全部文件成功");
                        }

                        @Override
                        public void onError(Throwable e) {
                            CommonLogger.printErrStackTrace(TAG, e, "删除apk下载路径全部文件失败");
                        }

                        @Override
                        public void onComplete() {

                        }

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
