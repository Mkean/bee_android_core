package com.bee.core.base;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bee.core.BuildConfig;
import com.bee.core.delegate.IComponentDescription;
import com.bee.core.logger.CommonLogConfig;
import com.bee.core.logger.CommonLogger;
import com.bee.core.spi.ComponentRegistry;
import com.bee.core.utils.AppUtils;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.TbsListener;

import java.io.File;

/**
 * @Description:
 */
public class BaseApplication extends Application {
    private static final String TAG = "BaseApplication";
    public static BaseApplication app;

    @Override
    public void onCreate() {
        super.onCreate();

        app = this;
        String packageName = AppUtils.getPackageName(this);
        Log.e("BaseApplication", "-----");
        Log.e("BaseApplication", "------应用包名-----" + packageName);
        String sign = AppUtils.getSign(this, packageName);
        Log.e("BaseApplication", "------应用签名，和新浪签名工具生成的值一样的------" + sign);
        Log.e("BaseApplication", "-----");

        initCommonLog();

        initARouter();

        initComponent();

        initX5(this);

        dispatchApplication();
    }

    private void initX5(Context context) {
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

            @Override
            public void onViewInitFinished(boolean arg0) {
                // TODO Auto-generated method stub
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                CommonLogger.d(TAG, " onViewInitFinished is " + arg0);
            }

            @Override
            public void onCoreInitFinished() {
                // TODO Auto-generated method stub
            }
        };
        //允许使用流量下载
        QbSdk.setDownloadWithoutWifi(true);
        //x5内核初始化接口
        QbSdk.initX5Environment(context, cb);
        //下载相关接口
        QbSdk.setTbsListener(new TbsListener() {
            @Override
            public void onDownloadFinish(int i) {
                CommonLogger.d(TAG, " onDownloadFinish i = " + i);
            }

            @Override
            public void onInstallFinish(int i) {
                CommonLogger.d(TAG, " onInstallFinish i = " + i);
            }

            @Override
            public void onDownloadProgress(int i) {
                CommonLogger.d(TAG, " onDownloadProgress i = " + i);
            }
        });
    }

    /**
     * 初始化路由
     */
    private void initARouter() {
        if (BuildConfig.DEBUG) {
            ARouter.openLog(); // 打开日志
            ARouter.openDebug(); // 开启调试模式（如果在InstanceRun模式下运行，必须开启调试模式！线上版本需要关闭，否则有安全风险）
        }
        ARouter.init(this); // 尽可能早，推荐在Application中初始化
    }

    /**
     * 收集组建
     */
    private void initComponent() {
        ComponentRegistry.Companion.loadComponents();
    }

    /**
     * 组件分发Application生命周期
     */
    private void dispatchApplication() {
        for (IComponentDescription iComponentDescription : ComponentRegistry.Companion.getAllComponents()) {

            if (iComponentDescription.getApplicationDelegate() != null) {

                iComponentDescription.getApplicationDelegate().onCreate(this);
            }
        }
    }

    /**
     * 初始化日志
     */
    private void initCommonLog() {
        String logPath = getApplicationContext().getExternalFilesDir(null).getAbsolutePath() + File.separator + "app_log";
        CommonLogConfig config = new CommonLogConfig()
                .setMaxFile(200)
                .setDay(7)
                .setCachePath(getApplicationContext().getFilesDir().getAbsolutePath())
                .setPath(logPath)
                .setEncryptKey16("2020161052010514".getBytes())
                .setEncryptIV16("2020161052010514".getBytes())
                .enableAppCrash(true)
                .enableDebug(true);
        CommonLogger.init(config);
    }
}
