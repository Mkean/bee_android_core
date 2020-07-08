package com.bee.android.common.app;

import android.app.Application;
import android.content.Context;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bee.android.common.BuildConfig;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.TbsListener;

public class BaseApplication extends Application {

    public static BaseApplication app;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        initARouter();
        initX5(this);
    }

    private void initARouter() {
        if (BuildConfig.DEBUG) {
            ARouter.openLog(); // 打开日志
            ARouter.openDebug(); // 开启调试模式（如果在InstanceRun模式下运行，必须开启调试模式！线上版本需要关闭，否则有安全风险）
        }
        ARouter.init(this);
    }

    private void initX5(Context context) {
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

            @Override
            public void onViewInitFinished(boolean b) {
                // TODO Auto-generated method stub
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
            }

            @Override
            public void onCoreInitFinished() {
                // TODO Auto-generated method stub
            }
        };

        // 允许使用流量下载
        QbSdk.setDownloadWithoutWifi(true);
        // x5内核初始化接口
        QbSdk.initX5Environment(context, cb);
        // 下载相关结果
        QbSdk.setTbsListener(new TbsListener() {
            @Override
            public void onDownloadFinish(int i) {

            }

            @Override
            public void onInstallFinish(int i) {

            }

            @Override
            public void onDownloadProgress(int i) {

            }
        });
    }
}
