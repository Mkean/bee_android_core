package com.bee.home.fragment.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bee.android.common.base.BaseFragment;
import com.bee.core.logger.CommonLogger;
import com.bee.home.R;
import com.bee.home.R2;
import com.bee.home.fragment.adapter.HomeCourseItemAdapter;
import com.bee.home.fragment.bean.HomeEventBusFragmentStateBean;
import com.bee.home.fragment.bean.HomeSubjectBean;
import com.bee.home.fragment.bean.HomeSubjectItemBean;
import com.bee.home.fragment.contract.HomeCourseContract;
import com.bee.home.fragment.presenter.HomeCoursePresenter;
import com.bee.home.fragment.utils.HomeCourseScrollUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @Description: 首页--课程卡片--fragment
 */
public class HomeCourseFragment extends BaseFragment<HomeCoursePresenter> implements HomeCourseContract.View {

    private static final String PARAM_BEAN = "paramBean";
    private static final String PARAM_POSITION = "paramPosition";

    @BindView(R2.id.home_course_item_rcy)
    RecyclerView recyclerView;

    private HomeCourseItemAdapter adapter;

    private HomeSubjectBean subjectBean;

    public static HomeCourseFragment newInstance(HomeSubjectBean subjectBean, int position) {
        HomeCourseFragment fragment = new HomeCourseFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(PARAM_BEAN, subjectBean);
        bundle.putInt(PARAM_POSITION, position);
        fragment.setArguments(bundle);
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
        CommonLogger.e("首页--可见状态", "创建HomeCourseFragment_onResume==" + getUserVisibleHint());
        // 视频播放，切换注册
        if (getUserVisibleHint()) {
            HomeCourseScrollUtil.getInstance().register(this);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        CommonLogger.e("首页--可见状态", "闯将HomeCourseFragment_onPause==" + getUserVisibleHint());

    }

    @Override
    protected HomeCoursePresenter getPresenter() {
        return new HomeCoursePresenter(this);
    }

    @Override
    protected void createView(View view) {
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
        }
    }

    private void setData(List<HomeSubjectItemBean> itemBeans) {
        CommonLogger.e("首页--创建", "创建HomeCourseFragment_setData==");
        if (itemBeans != null && itemBeans.size() > 0) {
            updateView(itemBeans);
        }
    }

    private void initRecyclerView() {
        adapter = new HomeCourseItemAdapter(null);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
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

    public void getSubjectData() {
        CommonLogger.e("首页--接口", "HomeCourseFragment_getSubjectData");
        if (mPresenter != null && subjectBean != null) {
            mPresenter.getSubjectData(subjectBean.getSubject_id());
        }
    }

    @Override
    public void setSubjectData(@Nullable ArrayList<HomeSubjectItemBean> itemBeans) {
        if (itemBeans != null && itemBeans.size() > 0) {
            setData(itemBeans);
        }
    }

    /**
     * 接收 体验课  倒计时结束
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventForHeaderAction(HomeEventBusFragmentStateBean event) {
        if (event != null && adapter != null) {
            CommonLogger.e("首页", "头像轮播接收==" + event.isShow());

            if (event.isShow()) {
                adapter.startExperienceViewHeader();
            } else {
                adapter.stopExperienceViewHeader();
            }
        }
    }
}
