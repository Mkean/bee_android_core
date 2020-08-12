package com.bee.home.fragment.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.bee.android.common.utils.AnimationUtil;
import com.bee.android.common.utils.WeakHandler;
import com.bee.core.glide.ImageLoadingUtil;
import com.bee.core.logger.CommonLogger;
import com.bee.home.R;
import com.bee.home.fragment.bean.HomeSubjectExperienceBannerBean;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @Description: 首页--课程卡片--底部轮播图效果
 */
public class HomeCourseBottomView extends ConstraintLayout {

    public static final String TAG = "HomeCourseBottomView";

    private final long DURATION_BIG = 2000;
    private final long ANIMATION_DURATION = 1000;

    private final int SHOW_POSITION_ONE = 1; // 当前显示位置：第一个
    private final int SHOW_POSITION_TWO = 2; // 当前显示位置：第二个
    private final int SHOW_POSITION_THREE = 3; // 当前显示位置：第三个

    private ImageView oneImg;
    private ImageView twoImg;
    private ImageView threeImg;

    private int currentLastPosition = -1;

    private List<String> urlList;

    private WeakHandler handler = new WeakHandler();
    private final Runnable task = new Runnable() {
        @Override
        public void run() {
            CommonLogger.e(TAG, "执行动画");
            initAnimation();
            if (handler != null) {
                handler.postDelayed(task, DURATION_BIG);
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
        oneImg = findViewById(R.id.view_course_bottom_one_iv);
        twoImg = findViewById(R.id.view_course_bottom_two_iv);
        threeImg = findViewById(R.id.view_course_bottom_three_iv);
    }

    public void setData(List<String> dataList) {
        if (dataList != null && dataList.size() > 0) {
            this.urlList = dataList;

            if (urlList.size() == 1) {
                // 单张
                CommonLogger.e(TAG, "单张展示");
                loadSingleUrl(urlList.get(0));
            } else {
                // 多张
                CommonLogger.e(TAG, "多张展示");
                loadMoreUrl();
            }
        } else {
            // 没有数据
            if (this.urlList != null) {
                this.urlList.clear();
            }
            CommonLogger.e(TAG, "没有数据展示");
        }
        loadSingleUrl("");
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
        twoImg.setVisibility(GONE);
        threeImg.setVisibility(GONE);
        loadImg(oneImg, url);
    }

    @SuppressLint("NewApi")
    private void loadMoreUrl() {
        if (urlList != null && urlList.size() > 1) {
            twoImg.setVisibility(VISIBLE);
            threeImg.setVisibility(VISIBLE);

            loadImg(oneImg, urlList.get(0));
            oneImg.setElevation(SHOW_POSITION_THREE);

            loadImg(twoImg, urlList.get(1));
            twoImg.setElevation(SHOW_POSITION_TWO);

            threeImg.setElevation(SHOW_POSITION_ONE);

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
        if (handler != null) {
            handler.removeCallbacks(task);
        }
    }

    public void startAutoPlay() {
        if (handler != null) {
            handler.removeCallbacks(task);
            handler.postDelayed(task, DURATION_BIG);
        }
    }

    private void initAnimation() {
        if (urlList != null && urlList.size() > 0) {
            // 有多个
            currentLastPosition++;
            if (currentLastPosition >= urlList.size()) {
                currentLastPosition = 0;
            }

            String url = urlList.get(currentLastPosition);

            animationAction(oneImg, url);
            animationAction(twoImg, url);
            animationAction(threeImg, url);
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
            AnimationUtil.alphaAnimation(imageView, 0f, 1.0f, ANIMATION_DURATION);
        }
    }

    private void animationImgHide(ImageView imageView) {
        if (imageView != null && imageView.getAlpha() == 1.0f) {
            AnimationUtil.alphaAnimation(imageView, 1.0f, 0f, ANIMATION_DURATION);
        }
    }
}
