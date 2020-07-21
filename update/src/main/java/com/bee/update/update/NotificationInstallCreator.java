package com.bee.update.update;

import android.app.Activity;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.bee.update.base.InstallNotifier;

import java.util.UUID;

/**
 * @Description:
 */
public class NotificationInstallCreator extends InstallNotifier {

    private final static String ACTION_INSTALL = "action.complete.install";
    private final static String ACTION_CANCEL = "action.complete.cancel";
    private RequestInstallReceiver requestInstallReceiver;
    public int id;
    public NotificationManager manager;
    NotificationCompat.Builder builder;

    @Override
    public Dialog create(Activity activity) {
        requestInstallReceiver = new RequestInstallReceiver();
        registerReceiver(activity);
        createNotification(activity);

        // 由于需要使用通知实现。此处返回null即可
        return null;
    }

    private void createNotification(Context context) {
        Object systemService = context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (systemService == null) {
            return;
        }
        this.manager = (NotificationManager) systemService;
        //高版本需要渠道
        if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            //只在Android O之上需要渠道，这里的第一个参数要和下面的channelId一样
            NotificationChannel notificationChannel = new NotificationChannel("default", "name", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.enableVibration(false);
            //如果这里用IMPORTANCE_NONE就需要在系统的设置里面开启渠道，通知才能正常弹出
            manager.createNotificationChannel(notificationChannel);
        }
        builder = new NotificationCompat.Builder(context, "default");
        builder.setAutoCancel(true)
                .setSmallIcon(context.getApplicationInfo().icon)
                .setContentTitle("小猴思维")
                .setContentText("APK安装包已下载完成，点击安装")
                .setDeleteIntent(createCancelPendingIntent(context))
                .setContentIntent(createOneShotPendingIntent(context));

        id = Math.abs(UUID.randomUUID().hashCode());
        Notification notification = builder.build();

        manager.notify(id, notification);

    }

    private PendingIntent createCancelPendingIntent(Context context) {
        Intent intent = new Intent();
        intent.setAction(ACTION_CANCEL);
        return PendingIntent.getBroadcast(context, 100, intent, PendingIntent.FLAG_CANCEL_CURRENT);
    }

    private PendingIntent createOneShotPendingIntent(Context context) {
        Intent intent = new Intent();
        intent.setAction(ACTION_INSTALL);
        return PendingIntent.getBroadcast(context, 100, intent, PendingIntent.FLAG_CANCEL_CURRENT);
    }

    public class RequestInstallReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            System.out.println(intent);
            unregisterReceiver(context);

            if (ACTION_INSTALL.equals(intent.getAction())) {
                // 发送安装请求。继续更新流程
                sendToInstall();
            } else {
                // 中断更新流程并通知用户取消更新
                sendUserCancel();
            }
            manager.cancel(id);
        }
    }

    private void registerReceiver(Context context) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_INSTALL);
        filter.addAction(ACTION_CANCEL);
        context.registerReceiver(requestInstallReceiver, filter);
    }

    private void unregisterReceiver(Context context) {
        context.unregisterReceiver(requestInstallReceiver);
    }
}
