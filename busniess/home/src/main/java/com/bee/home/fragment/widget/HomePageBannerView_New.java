package com.bee.home.fragment.widget;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.callback.NavCallback;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bee.android.common.utils.AnimationUtil;
import com.bee.android.common.view.banner.OnBannerListener;
import com.bee.android.common.web.config.H5WebConfig;
import com.bee.core.logger.CommonLogger;
import com.bee.core.utils.CommonUtil;
import com.bee.core.utils.ScreenUtil;
import com.bee.home.R;
import com.bee.home.fragment.animation.ScaleInPagerTransformer;
import com.bee.home.fragment.bean.HomeBannerBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 首页--顶部banner：自己写的轮播效果，真正使用的banner
 */
public class HomePageBannerView_New extends ConstraintLayout implements OnBannerListener, ViewPager.OnPageChangeListener {

    private Context context;

    private HomePageBanner banner;

    private List<HomeBannerBean> bannerBeans;

    boolean isClick = false;

    public HomePageBannerView_New(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public HomePageBannerView_New(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }

    public HomePageBannerView_New(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView();
    }

    private void initView() {
        View.inflate(context, R.layout.home_fragment_view_banner_new, this);
        banner = findViewById(R.id.home_view_banner_new);
        banner.setOffscreenPageLimit(5);
        banner.setPageTransformer(true, new ScaleInPagerTransformer());
        banner.setPageMargin(ScreenUtil.dip2px(10));
        banner.setOnBannerListener(this);
        banner.setOnPageChangeListener(this);
    }

    public void setBannerData(List<HomeBannerBean> data) {
        if (data != null && data.size() > 0) {
            this.bannerBeans = data;
            setVisibility(VISIBLE);
            List<String> urlList = new ArrayList<>();
            for (HomeBannerBean bean : bannerBeans) {
                if (bean != null) {
                    urlList.add(bean.getImg_url());
                }
            }
            banner.update(urlList);
        } else {
            setVisibility(GONE);
        }
    }

    @Override
    public void onBannerClick(int position) {
        if (bannerBeans != null && bannerBeans.size() > 0 && bannerBeans.size() > position) {
            HomeBannerBean bannerBean = bannerBeans.get(position);
            if (bannerBean != null) {
                if (isClick) {
                    return;

                }
                isClick = true;
                if (CommonUtil.isFastClick()) {
                    return;
                }
                CommonLogger.e("首页banner点击", "ARouter跳转之前");
                AnimationUtil.scaleAnimation(context, this);
                ARouter.getInstance().build(H5WebConfig.H5_ROUTER_WEB_ACTIVITY)
                        .withString(H5WebConfig.H5_PARAM_URL, bannerBean.getWeb_url())
                        .withString(H5WebConfig.H5_PARAM_DESC, "banner")
                        .navigation(context, new NavCallback() {
                            @Override
                            public void onArrival(Postcard postcard) {
                                new Handler().postDelayed(() -> {
                                    isClick = false;
                                }, 500);
                            }
                        });
                CommonLogger.e("首页banner点击", "ARouter跳转之后");
            }
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public void onStopBanner() {
        if (banner != null) {
            banner.stopAutoPlay();
        }
    }

    public void onStartBanner() {
        if (banner != null) {
            banner.startAutoPlay();
        }
    }

    public void release() {
        if (banner != null) {
            banner.onDestroy();
        }
    }
}
