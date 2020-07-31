package com.bee.launch.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bee.android.common.base.BaseActivity;
import com.bee.android.common.base.CommonApplication;
import com.bee.android.common.bean.UpdateBean;
import com.bee.android.common.component.ComponentTagKt;
import com.bee.android.common.config.CommonGlobalConfigKt;
import com.bee.android.common.config.ParamConfigKt;
import com.bee.android.common.dialog.UpdateApkDialog;
import com.bee.android.common.mmkv.MMKVUtils;
import com.bee.android.common.permission.PermissionUtils;
import com.bee.android.common.view.TabViewPager;
import com.bee.core.delegate.IComponentDescription;
import com.bee.core.logger.CommonLogger;
import com.bee.core.permission.config.PermissionStr;
import com.bee.core.spi.ComponentRegistry;
import com.bee.core.utils.AppUtils;
import com.bee.launch.R;
import com.bee.launch.R2;
import com.bee.launch.adapter.MainPagerAdapter;
import com.bee.launch.constant.ConstantsKt;
import com.bee.launch.constant.StoreKeyKt;
import com.bee.launch.constant.UrlConfigKt;
import com.bee.launch.contact.MainContact;
import com.bee.launch.manager.DialogAddressManager;
import com.bee.launch.manager.HomeDialogType;
import com.bee.launch.presenter.MainPresenter;
import com.bee.update.UpdateBuilder;
import com.google.android.material.tabs.TabLayout;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 *
 */
@Route(path = UrlConfigKt.LAUNCH_MAIN)
public class MainActivity extends BaseActivity<MainPresenter> implements MainContact.View, TabLayout.BaseOnTabSelectedListener {

    @Autowired(name = ParamConfigKt.MAIN_FROM)
    String mFrom;
    @Autowired(name = ParamConfigKt.MAIN_INDEX)
    String mIndex;

    private final String TAG = "MainActivity";
    private final String CURRENT_INDEX = "current_index";

    private List<String> mSelectImgs = new ArrayList<>();
    private List<String> mSelectJsons = new ArrayList<>();
    private List<String> mUnSelectImgs = new ArrayList<>();
    private List<String> mUnSelectJsons = new ArrayList<>();
    private List<Fragment> mFragments = new ArrayList<>();
    private List<IComponentDescription> mComponents = new ArrayList<>();

    private int mCurrentIndex = -1;
    private MainPagerAdapter adapter;

    /**
     * 升级
     */
    private UpdateApkDialog mUpdateApkDialog;
    private UpdateBuilder mUpdateBuilder;

    /**
     * 地址弹窗
     */
    private boolean isFirstDialog = true;
    private boolean isShowAddressDialog = true;
    private DialogAddressManager dialogAddressManager;


    @BindView(R2.id.bee_home_tab_vp)
    TabViewPager viewPager;
    @BindView(R2.id.bee_home_tab_layout)
    TabLayout tabLayout;


    @Override
    protected void onViewCreate() {
        CommonLogger.i(TAG, "onViewCreate()>>>from={},index={}", mFrom, mIndex);
        super.onViewCreate();

        initComponent();

        if (mPresenter != null) {
            mPresenter.getProtocolTimeStamp();
        }

        dispatchMainActivity();

        initX5Environment();
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected MainPresenter getPresenter() {
        return new MainPresenter(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CURRENT_INDEX, viewPager.getCurrentItem());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        mCurrentIndex = savedInstanceState.getInt(CURRENT_INDEX, -1);
        if (mCurrentIndex > -1 && mCurrentIndex < mFragments.size()) {
            // 选择 Fragment
            viewPager.setCurrentItem(mCurrentIndex);

            // 选择导航栏
            for (int i = 0; i < mFragments.size(); i++) {
                if (tabLayout != null) {
                    if (i == mCurrentIndex && tabLayout.getTabAt(mCurrentIndex) != null) {
                        changeTab(tabLayout.getTabAt(mCurrentIndex), true);
                    } else {
                        changeTab(tabLayout.getTabAt(i), false);
                    }
                }
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (CommonApplication.APP_STATUS != CommonApplication.APP_STATUS_NORMAL) {
            CommonLogger.i(TAG, "非正常启动流程，直接重新初始化应用界面");
            CommonApplication.reInitApp();
            finish();
            return;
        }

        if (intent != null) {
            int index = intent.getIntExtra(ParamConfigKt.MAIN_INDEX, -1);
            mFrom = intent.getStringExtra(ParamConfigKt.MAIN_FROM);
            CommonLogger.i(TAG, "onNewIntent>>>from={},index={}", mFrom, index);
            switchTab(index);
        }
    }

    private void switchTab(int index) {
        if (index > -1 && index < mFragments.size()) {
            viewPager.setCurrentItem(index, false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isFirstDialog) {
            showAddressDialog();
        }
    }

    /**
     * 初始化
     */
    private void initComponent() {
        List<IComponentDescription> components = ComponentRegistry.Companion.getComponents(ComponentTagKt.LAUNCHER);
        if (components.size() <= 0) {
            return;
        }

        for (IComponentDescription component : components) {
            if (component != null && !TextUtils.isEmpty(component.getEntranceRouteUrl()) && component.getComponentRes() != null) {
                try {
                    mFragments.add((Fragment) ARouter.getInstance().build(component.getEntranceRouteUrl()).navigation());
                    if (component.getComponentRes() != null) {
                        mSelectImgs.add(component.getComponentRes().getSelectImageAssetsFolder());
                        mSelectJsons.add(component.getComponentRes().getSelectAssetName());
                        mUnSelectImgs.add(component.getComponentRes().getUnSelectImageAssetsFolder());
                        mUnSelectJsons.add(component.getComponentRes().getUnSelectAssetName());
                    }
                    mComponents.add(component);
                } catch (Exception e) {
                    CommonLogger.printErrStackTrace(TAG, e, "初始化组件异常");
                }
            }
        }

        if (mFragments != null && mFragments.size() > 0) {
            adapter = new MainPagerAdapter(getSupportFragmentManager(), mFragments);
            viewPager.setAdapter(adapter);
            viewPager.setOffscreenPageLimit(mFragments.size());
            tabLayout.setupWithViewPager(viewPager);
            for (int i = 0; i < mFragments.size(); i++) {
                TabLayout.Tab tab = tabLayout.getTabAt(i);
                if (tab != null) {
                    tab.setCustomView(getTabView(this, i));
                }
            }

            tabLayout.addOnTabSelectedListener(this);
        }

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
        lottieAnimationView.setImageAssetsFolder(mSelectImgs.get(position));
        lottieAnimationView.setAnimation(mSelectJsons.get(position));
        if (position == 0) {
            lottieAnimationView.playAnimation();
        }
        return view;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        changeTab(tab, true);

        if (tab != null) {
            String name = "未知_" + tab.getPosition();

            if (mComponents != null && tab.getPosition() > -1 && tab.getPosition() < mComponents.size()) {
                IComponentDescription component = mComponents.get(tab.getPosition());
                if (component != null && !TextUtils.isEmpty(component.getName())) {
                    name = component.getName();
                }
            }

            CommonLogger.i(TAG, "onTabSelected>>>position={},name={}", tab.getPosition(), name);
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        // 选项卡退出时所选状态调用
        changeTab(tab, false);
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        // 当用户再次选择已选择的选项卡时调用
    }

    private void changeTab(TabLayout.Tab tab, boolean b) {
        if (tab != null) {
            View customView = tab.getCustomView();
            if (customView != null) {
                LottieAnimationView lottieAnimationView = customView.findViewById(R.id.main_tab_lottie_view);
                if (b) {
                    // 选中状态
                    lottieAnimationView.setImageAssetsFolder(mSelectImgs.get(tab.getPosition()));
                    lottieAnimationView.setAnimation(mSelectJsons.get(tab.getPosition()));
                    lottieAnimationView.playAnimation();
                    viewPager.setCurrentItem(tab.getPosition());
                } else {
                    // 非选中状态
                    lottieAnimationView.setImageAssetsFolder(mUnSelectImgs.get(tab.getPosition()));
                    lottieAnimationView.setAnimation(mUnSelectJsons.get(tab.getPosition()));
                    lottieAnimationView.playAnimation();
                }
            }
        }
    }

    @Override
    public void dialogFinish(@NotNull HomeDialogType currentDialogType) {

        CommonLogger.i(TAG, currentDialogType + "结束");

        switch (currentDialogType) {

            case DialogTypeUpdate:
                showAddressDialog();
                break;

            case DialogTypePrivacyAgreement:
                mPresenter.getAppUpdate(AppUtils.getVersionName(this), CommonGlobalConfigKt.APP_ID);
                break;

            case DialogTypeShowAddress:
                break;

        }
        ;
    }

    private void showPrivacyClauseDialog(long userDataAgreementTime, long userRegisterAgreementTime) {

    }

    private void showAddressDialog() {
        CommonLogger.i(TAG, "显示地址提醒弹窗");
        if (isShowAddressDialog) {
            if (dialogAddressManager == null) {
                dialogAddressManager = new DialogAddressManager();
                dialogAddressManager.showCheckDialog(this, isAgain -> {
                    isShowAddressDialog = isAgain;
                    dialogAddressManager = null;
                    dialogFinish(HomeDialogType.DialogTypeShowAddress);
                });
            }
        } else {
            dialogFinish(HomeDialogType.DialogTypeShowAddress);
        }
        isFirstDialog = false;


    }

    @Override
    public void updateProtocol(long userDataAgreementTime, long userRegisterAgreementTime) {
        CommonLogger.i(TAG, "协议服务器返回时间>>>userDataAgreementTime={},userRegisterAgreementTime={}", userDataAgreementTime, userRegisterAgreementTime);
        // 如果未显示过
        if (!MMKVUtils.getBoolean(ConstantsKt.LAUNCH_MMKV_ID, StoreKeyKt.IS_ALSO_SHOW_PRIVACY_AGREEMENT, false)) {
            showPrivacyClauseDialog(userDataAgreementTime, userRegisterAgreementTime);
            return;
        }
        // 1.更新隐私协议 2.更新注册协议 3.都更新
        int type = 0;
    }

    @Override
    public void showSuccess(@NotNull UpdateBean bean) {

    }

    @Override
    public void showFail() {

    }

    /**
     * 分发入口类生命周期
     */
    private void dispatchMainActivity() {
        for (IComponentDescription iComponentDescription : ComponentRegistry.Companion.getAllComponents()) {
            if (iComponentDescription.getActivityDelegate() != null) {
                iComponentDescription.getActivityDelegate().onCreate(this);
            }
        }
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