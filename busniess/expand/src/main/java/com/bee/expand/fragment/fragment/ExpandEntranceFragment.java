package com.bee.expand.fragment.fragment;

import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.bee.android.common.base.BaseFragment;
import com.bee.android.common.base.BasePresenter;
import com.bee.expand.R;
import com.bee.expand.fragment.constant.ConstantKt;

/**
 * @Description:
 */
@Route(path = ConstantKt.EXPAND_ENTRANCE_FRAGMENT)
public class ExpandEntranceFragment extends BaseFragment {
    @Override
    protected int getLayoutId() {
        return R.layout.expand_fragment_layout;
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
