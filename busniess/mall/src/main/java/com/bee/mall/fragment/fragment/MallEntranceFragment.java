package com.bee.mall.fragment.fragment;

import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.bee.android.common.base.BaseFragment;
import com.bee.android.common.base.BasePresenter;
import com.bee.mall.R;
import com.bee.mall.fragment.constant.ConstantKt;

/**
 * @Description:
 */
@Route(path = ConstantKt.MALL_ENTRANCE_FRAGMENT)
public class MallEntranceFragment extends BaseFragment {
    @Override
    protected int getLayoutId() {
        return R.layout.mall_fragment_layout;
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
