package com.bee.home.fragment.fragment;

import android.graphics.Rect;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.bee.android.common.animation.CustomAnimator;
import com.bee.android.common.base.BaseFragment;
import com.bee.android.common.base.CommonApplication;
import com.bee.android.common.view.MultipleStatusView;
import com.bee.android.common.view.pageState.PageStateRelativeLayout;
import com.bee.core.logger.CommonLogger;
import com.bee.core.utils.CommonUtil;
import com.bee.core.utils.NetWatchdog;
import com.bee.core.utils.ScreenUtil;
import com.bee.core.utils.StringUtils;
import com.bee.core.utils.ToastUtils;
import com.bee.core.utils.statusbar.StatusBarCompat;
import com.bee.home.R;
import com.bee.home.R2;
import com.bee.home.fragment.adapter.HomeAdapter;
import com.bee.home.fragment.bean.HomeEventBusExperienceTimeZero;
import com.bee.home.fragment.bean.HomeEventBusFragmentStateBean;
import com.bee.home.fragment.bean.HomeNativeBean;
import com.bee.home.fragment.constant.ConstantKt;
import com.bee.home.fragment.contract.HomeEntranceContract;
import com.bee.home.fragment.presenter.HomeEntrancePresenter;
import com.bee.home.fragment.utils.HomeCourseScrollUtil;
import com.bee.home.fragment.widget.HomePageAdapterHeaderView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import butterknife.BindView;

/**
 * @Description: 首页--组件入口---fragment
 */

@Route(path = ConstantKt.HOME_ENTRANCE_FRAGMENT)
public class HomeEntranceFragment extends BaseFragment<HomeEntrancePresenter>
        implements HomeEntranceContract.View, OnRefreshListener {

    @BindView(R2.id.home_frg_top_name_layout)
    RelativeLayout headerNameLayout;
    @BindView(R2.id.home_frg_top_name_tv)
    TextView headerNameTv;

    @BindView(R2.id.home_frg_smart_refresh_layout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R2.id.home_frg_rl)
    RecyclerView mRecyclerView;
    @BindView(R2.id.home_frg_page_state_layout)
    PageStateRelativeLayout pageStateLayout;

    @BindView(R2.id.home_frg_player_line)
    View lineView;

    private HomeAdapter adapter;

    private MultipleStatusView statusView;
    private StateViewListener listener = new StateViewListener();

    private boolean isFirstLoad = true; // 是否是第一次加载：onResume 方法判断
    private boolean isFirstVisibleToUserLoad = true; // 是否是第一次加载：setUserVisibleHint 方法判断

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
        pageStateLayout.showContent();
    }

    @Override
    protected void initEventAndData() {
        initHeaderLayout();
        initRecyclerView();
        initSmartRefreshLayout();
        if (smartRefreshLayout != null) {
            smartRefreshLayout.autoRefresh();
        }
    }

    @Override
    protected boolean registerEventBus() {
        return true;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        CommonLogger.e("首页--可见状态", "HomeEntranceFragment_setUserVisibleHint==" + isVisibleToUser);
        if (isVisibleToUser) {
            // 底部tab切换 可见
            HomeCourseScrollUtil.getInstance().videoPlayAction();
            startBannerScroll();
            eventBusSendFragmentShowState(true);
            errorViewAnimation(true);

            if (!isFirstVisibleToUserLoad) {
                CommonLogger.e("首页--可见状态", "HomeEntranceFragment_setUserVisibleHint_非第一次开赛数据");
                getServerData();
            }
            isFirstVisibleToUserLoad = false;
        } else {
            // 底部tab切换  不可见
            HomeCourseScrollUtil.getInstance().releasePlayAction();
            stopBannerScroll();
            eventBusSendFragmentShowState(false);
            errorViewAnimation(false);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        CommonLogger.e("首页--可见", "HomeEntranceFragment_onResume==" + getUserVisibleHint());
        // 前后台切换：可见
        if (getUserVisibleHint()) {
            HomeCourseScrollUtil.getInstance().videoOnResume();
            startBannerScroll();
            eventBusSendFragmentShowState(true);
            errorViewAnimation(true);

            if (!isFirstLoad) {
                // 不是第一次加载，每次前后台切换，都需要重新加载数据
                CommonLogger.e("首页--可见", "HomeEntranceFragment_onResume_非第一次加载数据");
                getServerData();
            }
            isFirstLoad = false;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        CommonLogger.e("首页--可见状态", "HomeEntranceFragment_onPause==" + getUserVisibleHint());
        // 前后台切换：不可见
        HomeCourseScrollUtil.getInstance().videoOnPause();

        stopBannerScroll();
        eventBusSendFragmentShowState(false);
        errorViewAnimation(false);
    }

    private void setHomeCourseScrollUtilY() {
        if (lineView != null) {
            int[] location = new int[2];
            lineView.getLocationOnScreen(location);

            HomeCourseScrollUtil.getInstance().setLineY(location[1]);
        }
    }

    private void initHeaderLayout() {
        if (headerNameLayout != null && headerNameTv != null) {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) headerNameTv.getLayoutParams();
            layoutParams.setMargins(0, StatusBarCompat.getStatusBarHeight(mContext), 0, 0);
            headerNameTv.setLayoutParams(layoutParams);

            headerNameLayout.setVisibility(View.INVISIBLE);
            CommonLogger.e("首页", "initHeaderLayout");
        }
    }

    private void initRecyclerView() {

        LinearLayoutManager recyclerViewManager = new LinearLayoutManager(getActivity()) {
            @Override
            protected int getExtraLayoutSpace(RecyclerView.State state) {
//                return super.getExtraLayoutSpace(state);
                return 300;
            }
        };
        recyclerViewManager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setLayoutManager(recyclerViewManager);
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                if (parent.getChildAdapterPosition(view) == 1) {
                    outRect.top = ScreenUtil.dip2px(mContext, 30);
                } else if (parent.getChildAdapterPosition(view) == 2) {
                    outRect.top = ScreenUtil.dip2px(mContext, 20);
                }
            }
        });

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int position = recyclerViewManager.findFirstVisibleItemPosition();
                if (position > 0) {
                    headerNameLayout.setVisibility(View.VISIBLE);
                } else {
                    headerNameLayout.setVisibility(View.INVISIBLE);
                }
            }
        });

        CommonLogger.e("滚动监听", "");
        if (HomeCourseScrollUtil.getInstance().getScrollListener() != null) {
            CommonLogger.e("滚动监听——设置监听器", "监听器设置成功");
            mRecyclerView.addOnScrollListener(HomeCourseScrollUtil.getInstance().getScrollListener());
        }
    }

    private void initAdapter(List<HomeNativeBean> data) {
        if (mRecyclerView != null) {
            adapter = new HomeAdapter(data, this);
            adapter.addHeaderView(new HomePageAdapterHeaderView(mContext));
            adapter.openLoadAnimation(new CustomAnimator());
            adapter.isFirstOnly(false);
            mRecyclerView.setAdapter(adapter);
        }
    }

    private void initSmartRefreshLayout() {
        smartRefreshLayout.setEnableRefresh(true);
        smartRefreshLayout.setOnRefreshListener(this);
        smartRefreshLayout.setEnableLoadMore(true);
        smartRefreshLayout.setEnableAutoLoadMore(false);
        smartRefreshLayout.setOnLoadMoreListener(refreshLayout -> refreshLayout.finishLoadMore(0));
        CommonLogger.e("首页", "initSmartRefreshLayout");
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        CommonLogger.e("首页", "onRefresh 请求接口");
        if (mPresenter != null) {
            mPresenter.getServerData();
        }
    }

    /**
     * 接收  体验课  倒计时结束
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventForTimeZero(HomeEventBusExperienceTimeZero event) {
        CommonLogger.e("首页", "体验课倒计时结束接收");
        getServerData();
    }

    private void getServerData() {
        CommonLogger.e("首页", "getServerData 请求接口");
        if (mPresenter != null) {
            mPresenter.getServerData();
        }
    }

    @Override
    public void finishRefreshAndLoadMore() {
        RefreshState state = smartRefreshLayout.getState();
        if (state.isHeader) {
            smartRefreshLayout.finishRefresh();
        } else if (state.isFooter) {
            smartRefreshLayout.finishLoadMore();
        }
    }

    @Override
    public void setServerData(List<HomeNativeBean> data) {
        if (data != null && data.size() > 0) {
            if (statusView != null) {
                adapter.removeHeaderView(statusView);
                statusView.release();
                statusView = null;
            }
            if (adapter == null) {
                initAdapter(data);
            } else {
                adapter.setNewData(data);
            }

            setHomeCourseScrollUtilY();

            new Handler().postDelayed(() -> {
                HomeCourseScrollUtil.getInstance().videoPlayAction();
            }, 500);
        }
    }

    @Override
    public void showErrorView(boolean isShow, @NotNull String code) {
        CommonLogger.e("首页--接口——showErrorView", "isShow ==" + isShow);
        CommonLogger.e("首页--接口——showErrorView", "code ==" + code);

        if (statusView == null) {
            statusView = new MultipleStatusView(mContext);
            statusView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
        }

        statusView.setOnViewClickListener(listener);

        String decTxt;

        // 正式数据
        if (!NetWatchdog.isNetAvailable(CommonApplication.app)) {
            decTxt = CommonApplication.app.getResources().getString(R.string.common_net_error);
        } else {
            decTxt = CommonApplication.app.getResources().getString(R.string.common_load_error);
        }

        if (!StringUtils.isNullOrEmpty(code)) {
            decTxt = decTxt + "(" + code + ")";
        }

        if (!StringUtils.isNullOrEmpty(decTxt)) {
            ToastUtils.show(decTxt);
            statusView.showError(decTxt);
        }

        errorViewAnimation(isShow);
        if (isShow) {
            if (statusView.getParent() == null) {
                adapter.addHeaderView(statusView);
                ((ViewGroup.MarginLayoutParams) statusView.getLayoutParams()).topMargin =
                        getResources().getDimensionPixelOffset(R.dimen.size_173dp);
            }
        } else {
            adapter.removeHeaderView(statusView);
            statusView.release();
            statusView = null;
        }
    }

    /**
     * 是否有数据
     *
     * @return
     */
    @Override
    public boolean isHasData() {
        if (adapter == null) {
            initAdapter(null);
        }
        return adapter != null && adapter.getItemCount() > 1;
    }

    /**
     * 开始顶部轮播图播放
     */
    private void startBannerScroll() {
        if (adapter != null) {
            adapter.startBanner();
        }
    }

    /**
     * 停止顶部轮播图播放
     */
    private void stopBannerScroll() {
        if (adapter != null) {
            adapter.stopBanner();
        }
    }

    private void errorViewAnimation(boolean isAnimation) {
        if (statusView != null && statusView.getParent() != null) {
            statusView.startLottieAnimation(isAnimation);
        }
    }

    /**
     * eventBus 发送，当fragment可见时
     * <p>
     * {@link HomeCourseFragment#onEventForHeaderAction(HomeEventBusFragmentStateBean)} 触发该方法
     *
     * @param isShow
     */
    private void eventBusSendFragmentShowState(boolean isShow) {
        EventBus.getDefault().post(new HomeEventBusFragmentStateBean(isShow));
    }

    private class StateViewListener implements MultipleStatusView.OnViewClickListener {


        @Override
        public void OnRetryClick() {
            if (CommonUtil.isFastClick()) {
                return;
            }

            if (mPresenter != null && smartRefreshLayout != null) {
                smartRefreshLayout.autoRefresh();
            }
        }

        @Override
        public void OnLoginClick() {

        }

        @Override
        public void OnChooseCourseClick() {

        }
    }
}
