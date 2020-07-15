package com.bee.launch.base;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bee.android.common.app.CommonApplication;
import com.bee.android.common.event.AppBackEvent;
import com.bee.android.common.event.AppFrontEvent;
import com.bee.android.common.logger.CommonLogger;
import com.bee.android.common.network.NetWorkManager;
import com.bee.android.common.network.config.okhttp.OkConfig;
import com.bee.launch.BuildConfig;
import com.bee.launch.network.config.UrlConfig;
import com.bee.launch.network.interceptor.CommonHeaderInterceptor;
import com.bee.launch.network.interceptor.DebugNotFoundInterceptor;
import com.bee.android.common.utils.CommonUtil;
import com.bee.android.common.view.smartRefresh.CommonRefreshBeeHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;

import org.greenrobot.eventbus.EventBus;

import java.util.LinkedList;

public class BaseApplication extends CommonApplication {
    public static final String TAG = "CommonApp";

    public static final int APP_STATUS_KILLED = 0; // 表示应用非正常启动
    public static final int APP_STATUS_NORMAL = 1; // 表示应用正常启动的
    public static final int APP_STATUS = APP_STATUS_NORMAL; // 记录APP启动状态

    public static LinkedList<Activity> store = new LinkedList<>();

    public NetworkStatusReceiver mNetworkStatusReceiver; // 用于监听网络改变

    public static boolean againShowUpdate = false; // 再次请求版本更新

    private static Handler mHandler = new Handler(Looper.getMainLooper());
    private static boolean buildMode = BuildConfig.DEBUG;

    @Override
    public void onCreate() {
        super.onCreate();
        initNetWork();
        initSmartRefresh();

        // 解决android P安装时弹窗
        CommonUtil.closeAndroidPDialog();

        // 添加Activity到任务栈
        registerActivityLifecycleCallbacks(new MonitorActivityStatusCallBack());
    }


    /**
     * 网络库初始化
     */
    private void initNetWork() {
        OkConfig okConfig = OkConfig.getOkConfig();
        // 连接超时时间
        okConfig.setConnectTimeOut(30)
                // 写入超时时间
                .setWriteTimeOut(60)
                // 读取超时时间
                .setReadTimeOut(60)
                // 支持最大并发数
                .setMaxRequests(6)
                .addInterceptor(new CommonHeaderInterceptor());
        if (buildMode) {
            okConfig.addInterceptor(new DebugNotFoundInterceptor());
        }

        // 初始化urlConfig
        UrlConfig.initUrl(buildMode);
        // 构建默认的全局网络库
        NetWorkManager.getInstance().initOkHttpClient(this, UrlConfig.GCX_URL);
    }

    /**
     * TODO：重新初始化应用，启动欢迎页
     */
    public static void reInitApp() {
    }

    private void initSmartRefresh() {
        SmartRefreshLayout.setDefaultRefreshHeaderCreator((context, layout) -> new CommonRefreshBeeHeader(context));
        SmartRefreshLayout.setDefaultRefreshFooterCreator((context, layout) -> new ClassicsFooter(context));
    }

    private class MonitorActivityStatusCallBack implements Application.ActivityLifecycleCallbacks {
        // 打开的Activity数量统计
        private int activityStartCount = 0;

        @Override
        public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
            CommonLogger.i(TAG, "onActivityCreated : " + activity.getClass().getSimpleName());
            if (store.size() == 0 && mNetworkStatusReceiver == null) {
                mNetworkStatusReceiver = new NetworkStatusReceiver();
            }
            store.add(activity);
        }

        @Override
        public void onActivityStarted(@NonNull Activity activity) {
            activityStartCount++;
            // 数值从0变1说明是从后台切换到前台
            if (activityStartCount == 1) {
                EventBus.getDefault().post(new AppFrontEvent());
            }
        }

        @Override
        public void onActivityResumed(@NonNull Activity activity) {

        }

        @Override
        public void onActivityPaused(@NonNull Activity activity) {

        }

        @Override
        public void onActivityStopped(@NonNull Activity activity) {
            activityStartCount--;
            //数值从1到0说明是从前台切到后台
            if (activityStartCount == 0) {
                EventBus.getDefault().post(new AppBackEvent());
            }
        }

        @Override
        public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(@NonNull Activity activity) {
            store.remove(activity);
            if (store.size() == 0 && mNetworkStatusReceiver != null) {
                try {
                    mNetworkStatusReceiver.unRegister();
                    mNetworkStatusReceiver = null;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 监听网络状态，可在联网恢复后自动加载数据
     */
    public static class NetworkStatusReceiver extends BroadcastReceiver {
        public NetworkStatusReceiver() {
            register();
        }

        public void unRegister() {
            app.unregisterReceiver(this);
        }

        private void register() {
            IntentFilter filter = new IntentFilter();
            filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            app.registerReceiver(this, filter);
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = cm.getActiveNetworkInfo();
            CommonLogger.i(TAG, "注册网络监听广播接收：" + info);
            if (info != null && info.isConnected() && !store.isEmpty()) {
                for (Activity activity : store) {
                    if (activity instanceof BaseActivity) {
                        ((BaseActivity) activity).onNetworkConnectedCore(info.getType());
                    }
                }
            }
        }
    }

    /**
     * 在主线程延迟执行
     *
     * @param runnable
     * @param delayMillis
     */
    public static void runOnUiThreadDelayed(Runnable runnable, long delayMillis) {
        mHandler.postDelayed(runnable, delayMillis);
    }
}
