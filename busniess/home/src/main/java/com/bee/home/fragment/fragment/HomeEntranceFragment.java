package com.bee.home.fragment.fragment;

import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.bee.android.common.base.BaseFragment;
import com.bee.android.common.base.BasePresenter;
import com.bee.home.R;
import com.bee.home.fragment.constant.ConstantKt;

/**
 * @Description:
 */

@Route(path = ConstantKt.HOME_ENTRANCE_FRAGMENT)
public class HomeEntranceFragment extends BaseFragment {
    @Override
    protected int getLayoutId() {
        return R.layout.home_fragment_layout;
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected void createView(View view) {

    }

    @Override
    protected void initEventAndData() {

    }
}
