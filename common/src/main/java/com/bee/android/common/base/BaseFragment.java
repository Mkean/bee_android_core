package com.bee.android.common.base;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bee.core.logger.CommonLogger;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseFragment<T extends BasePresenter> extends Fragment implements IView {

    private static final String TAG = "BaseFragment";

    protected Activity mActivity;
    protected Context mContext;
    protected T mPresenter;
    private Unbinder mUnBinder;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        mUnBinder = ButterKnife.bind(this, view);
        ARouter.getInstance().inject(this);
        mPresenter = getPresenter();
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }

        if (registerEventBus() && !EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        createView(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initEventAndData();
    }

    /**
     * 布局 id
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
     * View 创建时操作
     *
     * @param view
     */
    protected abstract void createView(View view);

    /**
     * activity 创建时操作
     */
    protected abstract void initEventAndData();

    private boolean registerEventBus() {
        return false;
    }

    @Override
    public void onDestroy() {
        if (mUnBinder != null) {
            mUnBinder.unbind();
        }
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (registerEventBus() && EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    /**
     * 每当网络连接后，回调这里
     *
     * @param type 网络类型 one of {@link ConnectivityManager#TYPE_MOBILE}, {@link
     *             ConnectivityManager#TYPE_WIFI}, {@link ConnectivityManager#TYPE_WIMAX}, {@link
     *             ConnectivityManager#TYPE_ETHERNET},  {@link ConnectivityManager#TYPE_BLUETOOTH}, or other
     *             types defined by {@link ConnectivityManager}
     */
    public void onNetworkConnected(int type) {
        CommonLogger.d(TAG, "NetworkConnected type: " + type);
        //个别情况下在子类收到该回调时会出现presenter为空的情况。猜测可能是手机的WIFI进入了睡眠，
        // 在客户端显示的瞬间网络状态发生了改变，而客户端在收到该通知时未能及时完成presenter创建，从而造成空指针
    }
}
