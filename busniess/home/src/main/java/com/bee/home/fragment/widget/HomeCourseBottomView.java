package com.bee.home.fragment.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.bee.android.common.utils.AnimationUtil;
import com.bee.android.common.view.banner.WeakHandler;
import com.bee.core.glide.ImageLoadingUtil;
import com.bee.core.logger.CommonLogger;
import com.bee.home.R;
import com.bee.home.fragment.bean.HomeSubjectExperienceBannerBean;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @Description: 首页---课程卡片，底图轮播效果
 */
public class HomeCourseBottomView extends ConstraintLayout {

    private final long DURATION_BIG = 2000;
    private final long ANIMATION_DURATION = 1000;

    private final int SHOW_POSITION_ONE = 1; // 当前显示位置：第一个
    private final int SHOW_POSITION_TWO = 2; // 当前显示位置：第二个
    private final int SHOW_POSITION_THREE = 3; // 当前显示位置：第三个

    private ImageView oneIv;
    private ImageView twoIv;
    private ImageView threeIv;

    private List<String> urlList;

    private WeakHandler mHandler = new WeakHandler();

    private int currentLastPosition = -1;

    private final Runnable task = new Runnable() {
        @Override
        public void run() {
            CommonLogger.e("HomeCourseBottomView", "执行动画");
            initAnimation();
            if (mHandler != null) {
                mHandler.postDelayed(task, DURATION_BIG);
            }
        }
    };

    public HomeCourseBottomView(Context context) {
        super(context);
        initView(context);
    }

    public HomeCourseBottomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public HomeCourseBottomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        View.inflate(context, R.layout.home_fragment_view_course_bottom, this);
        oneIv = findViewById(R.id.view_course_bottom_one_iv);
        twoIv = findViewById(R.id.view_course_bottom_two_iv);
        threeIv = findViewById(R.id.view_course_bottom_three_iv);
    }

    public void setData(List<String> dataList) {
        if (dataList != null && dataList.size() > 0) {
            this.urlList = dataList;
            if (urlList.size() == 1) {
                // 单张
                CommonLogger.e("HomeCourseBottomView", "单张展示");
                loadSingleUrl(urlList.get(0));
            } else {
                // 多张
                CommonLogger.e("HomeCourseBottomView", "多张展示");
                loadMoreUrl();
            }
        } else {
            if (this.urlList != null) {
                this.urlList.clear();
            }
            CommonLogger.e("HomeCourseBottomView", "没有数据展示");
            loadSingleUrl("");
        }
    }

    public void setDataForObject(List<HomeSubjectExperienceBannerBean> dataList) {
        List<String> list = new ArrayList<>();
        if (dataList != null && dataList.size() > 0) {
            for (HomeSubjectExperienceBannerBean bannerBean : dataList) {
                if (bannerBean != null) {
                    list.add(bannerBean.getBg_img_url());
                }
            }
        }
        setData(list);
    }

    private void loadSingleUrl(String url) {
        twoIv.setVisibility(GONE);
        threeIv.setVisibility(GONE);
        loadImg(oneIv, url);
    }

    @SuppressLint("NewApi")
    private void loadMoreUrl() {
        if (urlList != null && urlList.size() > 1) {
            twoIv.setVisibility(VISIBLE);
            threeIv.setVisibility(VISIBLE);

            loadImg(oneIv, urlList.get(0));
            oneIv.setElevation(SHOW_POSITION_ONE);

            loadImg(twoIv, urlList.get(1));
            twoIv.setElevation(SHOW_POSITION_TWO);

            threeIv.setElevation(SHOW_POSITION_ONE);

            currentLastPosition = 1;
        }
    }

    public boolean isCanPlay() {
        return urlList != null && urlList.size() > 1;
    }

    /**
     * 销毁资源
     */
    public void stopAutoPlay() {
        if (mHandler != null) {
            mHandler.removeCallbacks(task);
        }
    }

    public void startAutoPlay() {
        if (mHandler != null) {
            mHandler.removeCallbacks(task);
            mHandler.postDelayed(task, DURATION_BIG);
        }
    }

    private void initAnimation() {
        if (urlList != null && urlList.size() > 0) {
            currentLastPosition++;
            if (currentLastPosition >= urlList.size()) {
                currentLastPosition = 0;
            }

            String url = urlList.get(currentLastPosition);

            animationAction(oneIv, url);
            animationAction(twoIv, url);
            animationAction(threeIv, url);
        }
    }

    @SuppressLint("NewApi")
    private void animationAction(ImageView imageView, String url) {

        if (imageView != null) {
            try {
                int tag = (int) imageView.getElevation();
                switch (tag) {
                    case SHOW_POSITION_THREE:
                        // 第一个
                        // 执行 消失动画
                        animationImgHide(imageView);
                        Observable.just(ANIMATION_DURATION)
                                .delay(ANIMATION_DURATION, TimeUnit.MILLISECONDS)
                                .subscribeOn(Schedulers.computation())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(aLong -> {
                                    // 变化为第三个
                                    imageView.setElevation(SHOW_POSITION_ONE);
                                });
                        break;
                    case SHOW_POSITION_TWO:
                        // 第二个
                        Observable.just(ANIMATION_DURATION)
                                .delay(ANIMATION_DURATION, TimeUnit.MILLISECONDS)
                                .subscribeOn(Schedulers.computation())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(aLong -> {
                                    // 变化为第三个
                                    // 变化为第一个
                                    imageView.setElevation(SHOW_POSITION_THREE);
                                });
                        break;
                    case SHOW_POSITION_ONE:
                        // 第三个
                        // 加载图片
                        // 透明度变化为 显示
                        animationImgShow(imageView);
                        loadImg(imageView, url);
                        // 变化为第二个
                        imageView.setElevation(SHOW_POSITION_TWO);
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void loadImg(ImageView imageView, String url) {
        if (imageView != null) {
            ImageLoadingUtil.INSTANCE.loadImg(this, url, R.drawable.common_banner_default, R.drawable.common_default_big, imageView);
        }
    }

    private void animationImgShow(ImageView imageView) {
        if (imageView != null && imageView.getAlpha() == 0f) {
            imageView.setAlpha(1.0f);
        }
    }

    private void animationImgHide(ImageView imageView) {
        if (imageView != null && imageView.getAlpha() == 1.0f) {
            AnimationUtil.alphaAnimation(imageView, 1.0f, 0f, ANIMATION_DURATION);
        }
    }
}
