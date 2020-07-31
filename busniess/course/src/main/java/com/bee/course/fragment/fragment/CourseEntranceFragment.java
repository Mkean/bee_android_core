package com.bee.course.fragment.fragment;

import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.bee.android.common.base.BaseFragment;
import com.bee.android.common.base.BasePresenter;
import com.bee.course.R;
import com.bee.course.fragment.constant.ConstantKt;

/**
 * @Description:
 */
@Route(path = ConstantKt.COURSE_ENTRANCE_FRAGMENT)
public class CourseEntranceFragment extends BaseFragment {
    @Override
    protected int getLayoutId() {
        return R.layout.course_fragment_layout;
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
