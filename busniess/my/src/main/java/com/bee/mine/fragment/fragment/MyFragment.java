package com.bee.mine.fragment.fragment;

import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.bee.android.common.base.BaseFragment;
import com.bee.android.common.base.BasePresenter;
import com.bee.mine.R;
import com.bee.mine.fragment.constant.ConstantKt;

/**
 * @Description:
 */

@Route(path = ConstantKt.MY_ENTRANCE_FRAGMENT)
public class MyFragment extends BaseFragment {
    @Override
    protected int getLayoutId() {
        return R.layout.my_fragment_layout;
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
