package com.bee.launch.base;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bee.android.common.logger.CommonLogger;
import com.bee.android.common.utils.statusbar.StatusBarCompat;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseActivity<T extends BasePresenter> extends FragmentActivity implements IView {
    private static final String TAG = "BaseActivity";

    protected T mPresenter;
    protected Context context;
    private Unbinder mUnBinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (BaseApplication.APP_STATUS != BaseApplication.APP_STATUS_NORMAL) {
            CommonLogger.d(this.getClass().getSimpleName(), "非正常启动流程，直接重新初始化应用界面");
            BaseApplication.reInitApp();
            finish();
            return;
        }
        if (getIntent() != null) {
            onPrepareIntent(getIntent());
        }
        int layoutId = getLayoutId();
        if (layoutId != 0) {
            setContentView(layoutId);
        }

        context = this;
        mUnBinder = ButterKnife.bind(this);
        ARouter.getInstance().inject(this);
        mPresenter = getPresenter();
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
        if (registerEventBus() && !EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        onViewCreate();
    }

    @Override
    protected void onDestroy() {
        if (mUnBinder != null) {
            mUnBinder.unbind();
        }
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        if (registerEventBus() && EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }

    protected void onViewCreate() {
        StatusBarCompat.setStatusBarTranslucent(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            StatusBarCompat.setLightStatusBar(this, true);
            StatusBarCompat.setStatusBarColorFullScreen(this, Color.TRANSPARENT);
        } else {
            StatusBarCompat.setStatusBarColorFullScreen(this, Color.BLACK);
        }
    }

    /**
     * 布局id
     *
     * @return
     */
    protected abstract int getLayoutId();

    /**
     * 实现 Presenter
     *
     * @return
     */
    protected abstract T getPresenter();

    /**
     * intent下发，顺序在setContentView之前
     *
     * @param intent
     */
    protected void onPrepareIntent(Intent intent) {

    }

    protected boolean registerEventBus() {
        return false;
    }

    /**
     * 处理网络连接,广播分发
     *
     * @param type
     */
    public void onNetworkConnectedCore(int type) {
        onNetworkConnected(type);
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                if (fragment != null && fragment instanceof BaseFragment) {
                    ((BaseFragment) fragment).onNetworkConnected(type);
                }
            }
        }
    }

    /**
     * 每当网络连接后,回调到这里
     *
     * @param type 网络类型 one of {@link ConnectivityManager#TYPE_MOBILE}, {@link
     *             ConnectivityManager#TYPE_WIFI}, {@link ConnectivityManager#TYPE_WIMAX}, {@link
     *             ConnectivityManager#TYPE_ETHERNET},  {@link ConnectivityManager#TYPE_BLUETOOTH}, or other
     *             types defined by {@link ConnectivityManager}
     */
    protected void onNetworkConnected(int type) {
        CommonLogger.d(TAG, "NetworkConnected type: " + type);
    }


}
