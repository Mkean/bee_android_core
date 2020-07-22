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

import com.bee.core.logger.CommonLogger;
import com.bee.update.base.DownloadCallback;
import com.bee.update.base.DownloadNotifier;
import com.bee.update.model.Update;

import java.io.File;
import java.util.UUID;

/**
 * @Description: 下载失败，点击重试 notification
 */
public class NotificationDownloadErrorCreator extends DownloadNotifier {

    private final String TAG = "Update";

    private final static String ACTION_RETRY = "action.error.retry";
    private final static String ACTION_CANCEL = "action.error.cancel";
    private DownloadErrorReceiver requestInstallReceiver;
    NotificationManager manager;

    int id;

    @Override
    public DownloadCallback create(Update update, Activity activity) {
        requestInstallReceiver = new DownloadErrorReceiver();
        registerReceiver(activity);
        // 返回一个UpdateDownloadCB对象用于下载时使用来更新界面。
        return new NotificationCB(activity);
    }

    private class NotificationCB implements DownloadCallback {

        NotificationCompat.Builder builder;

        NotificationCB(Activity activity) {
            CommonLogger.d(TAG, "通知栏 下载失败，点击重试创建");


            manager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);


            //高版本需要渠道
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                //只在Android O之上需要渠道，这里的第一个参数要和下面的channelId一样
                NotificationChannel notificationChannel = new NotificationChannel("default", "name", NotificationManager.IMPORTANCE_HIGH);
                notificationChannel.enableLights(true);
                notificationChannel.enableVibration(false);
                //如果这里用IMPORTANCE_NONE就需要在系统的设置里面开启渠道，通知才能正常弹出
                manager.createNotificationChannel(notificationChannel);
            }
            builder = new NotificationCompat.Builder(activity, "default");
            builder
                    .setSmallIcon(activity.getApplicationInfo().icon)
                    .setWhen(System.currentTimeMillis())
                    .setAutoCancel(true)
                    .setContentTitle("小猴思维")
                    .setContentText("下载失败，点击重试.")
                    .setDeleteIntent(createCancelPendingIntent(activity))
                    .setContentIntent(createOneShotPendingIntent(activity))
                    .build();
            id = Math.abs(UUID.randomUUID().hashCode());
        }

        @Override
        public void onDownloadStart() {
            // 下载开始时的通知回调。运行于主线程
//            manager.notify(id,builder.build());
        }

        @Override
        public void onDownloadComplete(File file) {
            // 下载完成的回调。运行于主线程
//            manager.cancel(id);
        }

        @Override
        public void onDownloadProgress(long current, long total) {
        }

        @Override
        public void onDownloadError(Throwable t) {
            // 下载时出错。运行于主线程
            manager.notify(id, builder.build());

        }
    }

    private PendingIntent createCancelPendingIntent(Context context) {
        Intent intent = new Intent();
        intent.setAction(ACTION_CANCEL);
        return PendingIntent.getBroadcast(context, 100, intent, PendingIntent.FLAG_CANCEL_CURRENT);
    }

    private PendingIntent createOneShotPendingIntent(Context context) {
        Intent intent = new Intent();
        intent.setAction(ACTION_RETRY);
        return PendingIntent.getBroadcast(context, 100, intent, PendingIntent.FLAG_CANCEL_CURRENT);
    }

    public class DownloadErrorReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            System.out.println(intent);
            unregisterReceiver(context);

            if (ACTION_RETRY.equals(intent.getAction())) {
                // 下载失败点击重试
                restartDownload();

            } else {
                // cancel
            }
            manager.cancel(id);
        }
    }

    private void registerReceiver(Context context) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_RETRY);
        filter.addAction(ACTION_CANCEL);
        context.registerReceiver(requestInstallReceiver, filter);
    }

    private void unregisterReceiver(Context context) {
        context.unregisterReceiver(requestInstallReceiver);
    }
}
