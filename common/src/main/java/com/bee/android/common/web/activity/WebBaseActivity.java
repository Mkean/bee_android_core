package com.bee.android.common.web.activity;

import com.bee.android.common.base.BaseActivity;
import com.bee.android.common.base.BasePresenter;

/**
 * @Description:
 */
public class WebBaseActivity<T extends BasePresenter> extends BaseActivity {
    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }
}
