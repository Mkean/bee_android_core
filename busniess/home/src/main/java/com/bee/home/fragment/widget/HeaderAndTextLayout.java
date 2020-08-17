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
import com.bee.core.utils.ScreenUtil;
import com.bee.core.utils.StringUtils;
import com.bee.home.R;
import com.bee.home.fragment.bean.HomeSubjectCardSaleUserBean;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @Description: 首页---头像轮播View
 */
public class HeaderAndTextLayout extends ConstraintLayout {
    private final long DURATION = 500;
    private final long DURATION_BIG = 3000;

    private ImageView oneImg;
    private ImageView twoImg;
    private ImageView threeImg;
    private ImageView fourImg;

    private AnimationTextView textTv;

    private float[] endX;
    private float[] leftX;

    private List<HomeSubjectCardSaleUserBean> dataList;
    private int currentLastPosition = -1;
    private boolean isAfter;

    private WeakHandler mHandler = new WeakHandler();

    private final Runnable task = new Runnable() {
        @Override
        public void run() {
            initAnimation();
            if (mHandler != null) {
                mHandler.postDelayed(task, DURATION_BIG);
            }
        }
    };


    public HeaderAndTextLayout(Context context) {
        super(context);
        initView();
        initIndexX();
    }

    public HeaderAndTextLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
        initIndexX();
    }

    public HeaderAndTextLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
        initIndexX();
    }

    private void initView() {
        View.inflate(getContext(), R.layout.home_fragment_view_header_and_text, this);

        oneImg = findViewById(R.id.view_header_text_header_one_iv);
        oneImg.setVisibility(INVISIBLE);
        twoImg = findViewById(R.id.view_header_text_header_two_iv);
        twoImg.setVisibility(INVISIBLE);
        threeImg = findViewById(R.id.view_header_text_header_three_iv);
        threeImg.setVisibility(INVISIBLE);
        fourImg = findViewById(R.id.view_header_text_header_four_iv);
        fourImg.setVisibility(INVISIBLE);
        textTv = findViewById(R.id.view_header_text_txt_tv);
        textTv.setVisibility(INVISIBLE);
    }

    private void initIndexX() {
        if (endX == null || endX.length == 0) {
            endX = new float[3];
            endX[0] = ScreenUtil.dip2px(getContext(), 14);
            endX[1] = endX[0];
            endX[2] = -endX[0] * 2;
        }
        if (leftX == null || leftX.length == 0) {
            leftX = new float[3];
            leftX[0] = 0;
            leftX[1] = ScreenUtil.dip2px(getContext(), 14);
            leftX[2] = leftX[1] * 2;
        }
//        LogUtils.e("endX[0]== ", endX[0] + "");
//        LogUtils.e("endX[1]== ", endX[1] + "");
//        LogUtils.e("endX[2]== ", endX[2] + "");
//        LogUtils.e("leftX[0]== ", leftX[0] + "");
//        LogUtils.e("leftX[1]== ", leftX[1] + "");
//        LogUtils.e("leftX[2]== ", leftX[2] + "");
    }

    public void startAutoPLay() {
        if (mHandler != null) {
            mHandler.removeCallbacks(task);
            mHandler.postDelayed(task, DURATION_BIG);
        }
    }

    /**
     * 销毁资源
     */
    public void stopAutoPlay() {
        if (mHandler != null) {
            mHandler.removeCallbacks(task);
        }
    }

    public void setData(List<HomeSubjectCardSaleUserBean> dataList) {
//        dataList = test();
        if (dataList != null && dataList.size() > 0) {
            this.dataList = dataList;
            setVisibility(VISIBLE);
            startAutoPLay();
        } else {
            if (this.dataList != null) {
                this.dataList.clear();
            }
            setVisibility(INVISIBLE);
        }
    }

    private void initAnimation() {
        if (dataList != null && dataList.size() > 0) {
            currentLastPosition++;
            if (isAfter) {
                startAnimation(oneImg);
                startAnimation(twoImg);
                startAnimation(threeImg);
                startAnimation(fourImg);
            } else {
                switch (currentLastPosition) {
                    case 0:
                        startAnimation(oneImg);
                        break;
                    case 1:
                        startAnimation(oneImg);
                        startAnimation(twoImg);
                        break;
                    case 2:
                        startAnimation(oneImg);
                        startAnimation(twoImg);
                        startAnimation(threeImg);
                        break;
                    default:
                        startAnimation(oneImg);
                        startAnimation(twoImg);
                        startAnimation(threeImg);
                        startAnimation(fourImg);
                        isAfter = true;
                        break;
                }
            }
            textTvAnimation();
        }
    }

    @SuppressLint({"CheckResult", "NewApi"})
    private void startAnimation(ImageView imageView) {
        if (imageView.getVisibility() == VISIBLE) {
            //如果是展示状态，那么就是显示给用户的 三个中的一个  0
            if (imageView.getX() == leftX[0]) {
                //如果是第一个
                imageView.setElevation(3);
                AnimationUtil.trainsAnimation(imageView, imageView.getTranslationX(), endX[0], DURATION);
            } else if (imageView.getX() == leftX[1]) {
                //如果是第二个   42
                imageView.setElevation(2);
                AnimationUtil.trainsAnimation(imageView, imageView.getTranslationX(), endX[1], DURATION);
            } else if (imageView.getX() == leftX[2]) {
                //如果是第三个,则走隐藏  84
                imageView.setElevation(1);
                AnimationUtil.scaleAnimationSmall(imageView, DURATION);

                Observable.just(DURATION)
                        .delay(DURATION, TimeUnit.MILLISECONDS)
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(aLong -> {
                            imageView.setVisibility(INVISIBLE);
                            AnimationUtil.trainsAnimation(imageView, imageView.getTranslationX(), endX[2], DURATION);
                        });
            }
        } else {
            //如果是非展示状态，那么 就是用户看不到的那个，要去第一个位置
            setImageData(imageView);
            imageView.setElevation(4);
            imageView.setVisibility(VISIBLE);
            AnimationUtil.scaleAnimationBig(imageView, DURATION);
        }
    }

    private void textTvAnimation() {
        if (dataList != null && dataList.size() > 0) {
            if (currentLastPosition < 0 || currentLastPosition > dataList.size() - 1) {
                currentLastPosition = 0;
            }
            HomeSubjectCardSaleUserBean bean = dataList.get(currentLastPosition);
            if (bean != null) {
                textTv.setText(StringUtils.getFiltedNullStr(bean.getShow_title()));
            } else {
                textTv.setText("");
            }
            textTv.setVisibility(VISIBLE);
            textTv.startAnimation(AnimationUtil.setScaleAnimationForLeftBottom(DURATION));
        }
    }

    private void setImageData(ImageView img) {

        if (dataList != null && dataList.size() > 0) {
            if (currentLastPosition < 0 || currentLastPosition > dataList.size() - 1) {
                currentLastPosition = 0;
            }
            HomeSubjectCardSaleUserBean bean = dataList.get(currentLastPosition);
            if (bean != null) {
                ImageLoadingUtil.INSTANCE.loadImgForCircle(this,
                        bean.getHeader_url(),
                        R.drawable.home_fragment_icon_head,
                        R.drawable.home_fragment_icon_head,
                        img);
            } else {
                img.setBackgroundResource(R.drawable.home_fragment_icon_head);
            }
        } else {
            img.setBackgroundResource(R.drawable.home_fragment_icon_head);
        }
    }
}
