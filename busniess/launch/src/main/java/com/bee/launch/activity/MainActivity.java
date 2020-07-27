package com.bee.launch.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.airbnb.lottie.LottieAnimationView;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.bee.android.common.base.BaseActivity;
import com.bee.android.common.base.BasePresenter;
import com.bee.android.common.permission.PermissionUtils;
import com.bee.android.common.view.TabViewPager;
import com.bee.core.logger.CommonLogger;
import com.bee.core.permission.config.PermissionStr;
import com.bee.launch.R;
import com.bee.launch.R2;
import com.bee.launch.adapter.MainPagerAdapter;
import com.bee.launch.constant.UrlConfigKt;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

import butterknife.BindView;

/**
 * TODO：fragment给布局，方法数
 */
@Route(path = UrlConfigKt.LAUNCH_MAIN)
public class MainActivity extends BaseActivity implements TabLayout.BaseOnTabSelectedListener {

    private final String TAG = "MainActivity";
    private final String SKIP_TAG = "SKIP-MainActivity";

    private final String[] mSelectJsons = new String[]{
            "home/home_shouye_un_se/shouye2.json",
            "home/home_source_un_se/kecheng2.json",
            "home/home_training_un_se/xunlian2.json",
            "home/home_shop_un_se/shop2.json",
            "home/home_me_un_se/wode2.json"};
    private final String[] mSelectImgs = new String[]{
            "home/home_shouye_un_se/images/",
            "home/home_source_un_se/images/",
            "home/home_training_un_se/images/",
            "home/home_shop_un_se/images/",
            "home/home_me_un_se/images/"};

    private final String[] mUnSeJsons = new String[]{
            "home/home_shouye_se_un/shouye1.json",
            "home/home_source_se_un/kecheng1.json",
            "home/home_training_se_un/xunlian1.json",
            "home/home_shop_se_un/shop1.json",
            "home/home_me_se_un/wode1.json"};
    private final String[] mUnSeImgs = new String[]{
            "home/home_shouye_se_un/images/",
            "home/home_source_se_un/images/",
            "home/home_training_se_un/images/",
            "home/home_shop_se_un/images/",
            "home/home_me_se_un/images/"};

    @BindView(R2.id.bee_home_tab_vp)
    TabViewPager viewPager;
    @BindView(R2.id.bee_home_tab_layout)
    TabLayout beeHomeTabLayout;

    private String[] mTitles;

    private MainPagerAdapter adapter;
    private int mCurrentIndex = -1;


    @Override
    protected void onViewCreate() {
        CommonLogger.i(TAG, "-------------onViewCreate()");
        super.onViewCreate();

        initTitleName();
        initTabLayout();

        initX5Environment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    private void initTitleName() {
        mTitles = new String[5];
        mTitles[0] = "首页";
        mTitles[1] = "课程";
        mTitles[2] = "训练";
        mTitles[3] = "商城";
        mTitles[4] = "我的";
    }

    /**
     * 获取 tab 显示内容
     *
     * @param context
     * @param position
     * @return
     */
    private View getTabView(Context context, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.launch_main_tab_view, null);
        LottieAnimationView lottieAnimationView = view.findViewById(R.id.main_tab_lottie_view);
        lottieAnimationView.setImageAssetsFolder(mSelectImgs[position]);
        lottieAnimationView.setAnimation(mSelectJsons[position]);
        if (position == 0) {
            lottieAnimationView.playAnimation();
        }
        return view;
    }

    private void initTabLayout() {
        adapter = new MainPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(5);

        beeHomeTabLayout.setupWithViewPager(viewPager);

        for (int i = 0; i < adapter.MAX_COUNT; i++) {
            TabLayout.Tab tab = beeHomeTabLayout.getTabAt(i);

            if (tab != null) {
                tab.setCustomView(getTabView(context, i));
            }
        }
        beeHomeTabLayout.addOnTabSelectedListener(this);
    }


    @Override
    public void onTabSelected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        //
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        // 当用户再次选择已选择的选项卡时调用
    }

    /**
     * 初始化 X5
     */
    private void initX5Environment() {
        PermissionUtils.requestPermission(this, new PermissionUtils.RequestPermission() {
            @Override
            public void onRequestPermissionSuccess() {

            }

            @Override
            public void onRequestPermissionFailure(List<String> permissionDeny, List<String> permissionNoAsk) {

            }
        }, PermissionStr.READ_EXTERNAL_STORAGE, PermissionStr.WRITE_EXTERNAL_STORAGE);
    }
}