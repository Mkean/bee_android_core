package com.bee.home.fragment.fragment;

import android.view.View;

import androidx.annotation.NonNull;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.bee.android.common.base.BaseFragment;
import com.bee.android.common.base.BasePresenter;
import com.bee.home.R;
import com.bee.home.fragment.bean.HomeNativeBean;
import com.bee.home.fragment.constant.ConstantKt;
import com.bee.home.fragment.contract.HomeEntranceContract;
import com.bee.home.fragment.presenter.HomeEntrancePresenter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

/**
 * @Description: 首页组件入口 Fragment
 */

@Route(path = ConstantKt.HOME_ENTRANCE_FRAGMENT)
public class HomeEntranceFragment extends BaseFragment<HomeEntrancePresenter>
        implements HomeEntranceContract.View, OnRefreshListener {


    @Override
    protected int getLayoutId() {
        return R.layout.home_fragment_layout;
    }

    @Override
    protected HomeEntrancePresenter getPresenter() {
        return new HomeEntrancePresenter(this);
    }

    @Override
    protected void createView(View view) {
        initView(view);
    }

    @Override
    protected void initEventAndData() {

    }

    @Override
    protected boolean registerEventBus() {
        return true;
    }

    private void initView(View view) {

    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {

    }

    @Override
    public void finishRefreshAndLoadMore() {

    }

    @Override
    public void setServerData(List<HomeNativeBean> data) {

    }

    @Override
    public void showErrorView(boolean isShow, String code) {

    }

    @Override
    public boolean isHasData() {

        return false;
    }
}
