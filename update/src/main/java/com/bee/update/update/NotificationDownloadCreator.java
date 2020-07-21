package com.bee.update.update;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bee.android.common.logger.CommonLogger;
import com.bee.update.UrlConfig;
import com.bee.update.base.DownloadCallback;
import com.bee.update.base.DownloadNotifier;
import com.bee.update.impl.DefaultDownloadNotifier;
import com.bee.update.model.Update;

import java.io.File;
import java.util.UUID;

/**
 * @Description: 在通知栏进行进度条显示功能
 * 此类用于提供此种需求的解决方案。以及如何对其进行定制。满足任意场景使用
 * 默认使用参考：{@link DefaultDownloadNotifier}
 */
public class NotificationDownloadCreator extends DownloadNotifier {
    private final String TAG = "Update";
    private final static String ACTION_GO_MAIN = "action.into.main";
    public NotificationManager manager;
    int id;
    DownloadingToMainReceiver mDownloadingToMainReceiver;
    Activity activity;

    public class DownloadingToMainReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            System.out.println(intent);

            if (ACTION_GO_MAIN.equals(intent.getAction())) {
                // 下载中跳转到首页
                CommonLogger.d(TAG, "点击跳转到主页面");
                ARouter.getInstance().build(UrlConfig.LAUNCH_MAIN).navigation();
            } else {
                // cancel
            }
            manager.cancel(id);
        }
    }

    private void registerReceiver(Context context) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_GO_MAIN);
        context.getApplicationContext().registerReceiver(mDownloadingToMainReceiver, filter);
    }

    public void unregisterReceiver(Context context) {
        if (mDownloadingToMainReceiver != null) {
            try {
                context.getApplicationContext().unregisterReceiver(mDownloadingToMainReceiver);
                mDownloadingToMainReceiver = null;
            } catch (Exception e) {
                CommonLogger.printErrStackTrace(TAG, e, "取消广播注册异常");
            }
        }

    }

    private PendingIntent createOneShotPendingIntent(Context context) {
        Intent intent = new Intent();
        intent.setAction(ACTION_GO_MAIN);
        return PendingIntent.getBroadcast(context, 100, intent, PendingIntent.FLAG_CANCEL_CURRENT);
    }

    @Override
    public DownloadCallback create(Update update, Activity activity) {
        this.activity = activity;
        mDownloadingToMainReceiver = new DownloadingToMainReceiver();

        registerReceiver(activity);

        // 返回一个UpdateDownloadCB对象用于下载时使用来更新界面。
        return new NotificationCB(activity);
    }

    private class NotificationCB implements DownloadCallback {

        NotificationCompat.Builder builder;
        int preProgress;

        NotificationCB(Activity activity) {
            CommonLogger.d(TAG, "通知栏进度条下载");


            manager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);


            //高版本需要渠道
            if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                //只在Android O之上需要渠道，这里的第一个参数要和下面的channelId一样
                NotificationChannel notificationChannel = new NotificationChannel("default", "name", NotificationManager.IMPORTANCE_HIGH);
                notificationChannel.enableLights(true);
                notificationChannel.enableVibration(false);
                //如果这里用IMPORTANCE_NONE就需要在系统的设置里面开启渠道，通知才能正常弹出
                manager.createNotificationChannel(notificationChannel);
            }
            builder = new NotificationCompat.Builder(activity, "default");
            builder.setProgress(100, 0, false)
                    .setSmallIcon(activity.getApplicationInfo().icon)
                    .setWhen(System.currentTimeMillis())
                    .setAutoCancel(false)
                    .setContentText("下载中...")
                    .setContentIntent(createOneShotPendingIntent(activity))
                    .build();
            id = Math.abs(UUID.randomUUID().hashCode());
        }

        @Override
        public void onDownloadStart() {
            // 下载开始时的通知回调。运行于主线程
            manager.notify(id, builder.build());
        }

        @Override
        public void onDownloadComplete(File file) {
            if (mDownloadingToMainReceiver != null) {
                unregisterReceiver(activity);
            }
            // 下载完成的回调。运行于主线程
            manager.cancel(id);
        }

        @Override
        public void onDownloadProgress(long current, long total) {
            // 下载过程中的进度信息。在此获取进度信息。运行于主线程
            int progress = (int) (current * 1f / total * 100);
            // 过滤不必要的刷新进度
            if (preProgress < progress) {
                preProgress = progress;
                builder.setProgress(100, progress, false);
                builder.setOnlyAlertOnce(true);
                manager.notify(id, builder.build());
            }
        }

        @Override
        public void onDownloadError(Throwable t) {
            // 下载时出错。运行于主线程
            manager.cancel(id);
        }
    }
}
