package com.bee.home.fragment.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bee.android.common.base.BaseFragment;
import com.bee.core.logger.CommonLogger;
import com.bee.home.R;
import com.bee.home.fragment.adapter.HomeCourseItemAdapter;
import com.bee.home.fragment.bean.HomeSubjectBean;
import com.bee.home.fragment.bean.HomeSubjectItemBean;
import com.bee.home.fragment.contract.HomeCourseContract;
import com.bee.home.fragment.presenter.HomeCoursePresenter;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 首页---课程卡片-fragment
 */
public class HomeCourseFragment extends BaseFragment<HomeCoursePresenter> implements HomeCourseContract.View {

    private static final String PARAM_BEAN = "paramBean";
    private static final String PARAM_POSITION = "paramPosition";

    private RecyclerView recyclerView;
    private HomeCourseItemAdapter adapter;

    private HomeSubjectBean subjectBean;
    private int position; // fragment 的角标位

    public HomeCourseFragment() {
    }

    public static HomeCourseFragment newInstance(HomeSubjectBean subjectBean, int position) {
        HomeCourseFragment fragment = new HomeCourseFragment();
        Bundle args = new Bundle();
        args.putSerializable(PARAM_BEAN, subjectBean);
        args.putInt(PARAM_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.home_fragment_view_course_item;
    }

    @Override
    protected boolean registerEventBus() {
        return true;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        CommonLogger.e("首页--可见状态", "创建HomeCourseFragment_isVisibleToUser==" + isVisibleToUser);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    protected HomeCoursePresenter getPresenter() {
        return new HomeCoursePresenter(this);
    }

    @Override
    protected void createView(View view) {
        recyclerView = view.findViewById(R.id.home_course_item_rcy);
        initRecyclerView();
        CommonLogger.e("首页--创建", "创建HomeCourseFragment_createView");
    }

    @Override
    protected void initEventAndData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            Serializable serializable = bundle.getSerializable(PARAM_BEAN);
            if (serializable instanceof HomeSubjectBean) {
                subjectBean = (HomeSubjectBean) serializable;
                if (subjectBean.getCourses() != null && subjectBean.getCourses().size() > 0) {
                    setData(subjectBean.getCourses());
                }
            }

            position = bundle.getInt(PARAM_POSITION);
        }
    }

    private void setData(List<HomeSubjectItemBean> itemBeans) {
        CommonLogger.e("首页--创建", "创建HomeCourseFragment_setData");
        if (itemBeans != null && itemBeans.size() > 0) {
            updateView(itemBeans);
        }
    }

    private void initRecyclerView() {
        adapter = new HomeCourseItemAdapter(null);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void updateView(List<HomeSubjectItemBean> itemBeans) {
        if (subjectBean != null && itemBeans != null && itemBeans.size() > 0) {
            adapter.setNewData(itemBeans, subjectBean.getSubject_name());
        }
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public void getSubjectBean() {
        CommonLogger.e("首页--接口", "HomeCourseFragment_getSubjectData");
        if (mPresenter != null && subjectBean != null) {
            mPresenter.getSubjectData(subjectBean.getSubject_id());
        }
    }

    @Override
    public void setSubjectData(@NotNull ArrayList<HomeSubjectItemBean> itemBeans) {
        if (itemBeans != null && itemBeans.size() > 0) {
            setData(itemBeans);
        }
    }
}
