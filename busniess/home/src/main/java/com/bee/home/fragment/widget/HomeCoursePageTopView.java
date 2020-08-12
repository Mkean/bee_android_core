package com.bee.home.fragment.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.bee.android.common.utils.AnimationUtil;
import com.bee.core.glide.ImageLoadingUtil;
import com.bee.core.logger.CommonLogger;
import com.bee.core.utils.StringUtils;
import com.bee.home.R;
import com.bee.home.fragment.bean.HomeSubjectExperienceBannerBean;
import com.bee.home.fragment.listener.HomeCoursePageTopViewClickListener;
import com.bee.home.fragment.listener.HomeJzCommonPlayerListener;
import com.bee.home.fragment.listener.HomeSubjectBeanInterface;

import java.util.List;

import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;

import static com.bee.home.fragment.bean.HomeSubjectExperienceBannerBean.banner_type_image;
import static com.bee.home.fragment.bean.HomeSubjectExperienceBannerBean.banner_type_video;

/**
 * @Description: 首页--体验课和系统课通用--头部View
 */
public class HomeCoursePageTopView extends ConstraintLayout implements HomeJzCommonPlayerListener, View.OnClickListener {

    private final long ANIMATION_DURATION = 1000;

    private Context context;

    private TextView titleNameTv; // "体验课"
    private TextView discountNameTv; // "限时赠送249元代金券"
    private TextView classOpenDateTv; // "9月9日开课"
    private TextView pointTv; // "适合幼小衔接"

    private TextView priceUnitTv; // "两周"
    private TextView priceNewTv; // "9.9"
    private TextView priceOldTv; // "149"

    private ImageView playerImg; // 视频封面
    private HomeJzCommonPlayer jzCommonPlayer;
    private HomeCourseBottomView bottomView;

    private String mVideoUrl;

    private View clickView;

    private HomeCoursePageTopViewClickListener listener;

    private ObjectAnimator animatorHide;

    public HomeCoursePageTopView(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public HomeCoursePageTopView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }

    public HomeCoursePageTopView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.context = context;
        initView();
    }

    private void initView() {
        View.inflate(context, R.layout.home_fragment_view_course_top, this);

        titleNameTv = findViewById(R.id.home_course_experience_name_tv);
        discountNameTv = findViewById(R.id.home_course_discount_name_tv);
        classOpenDateTv = findViewById(R.id.home_course_class_open_date_tv);
        pointTv = findViewById(R.id.home_course_experience_point_tv);
        pointTv.setVisibility(GONE);

        priceUnitTv = findViewById(R.id.home_course_price_unit_tv);
        priceNewTv = findViewById(R.id.home_course_price_new_tv);
        priceOldTv = findViewById(R.id.ch_view_item_price_old_tv);
        priceOldTv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

        playerImg = findViewById(R.id.ch_view_item_experience_show_vp_iv);
        playerImg.setVisibility(GONE);

        jzCommonPlayer = findViewById(R.id.ch_view_item_experience_show_vp);
        jzCommonPlayer.setVisibility(GONE);
        jzCommonPlayer.setHomeJzCommonPlayerListener(this);
        // 根据自己情况选择一个  填充
        Jzvd.setVideoImageDisplayType(Jzvd.VIDEO_IMAGE_DISPLAY_TYPE_FILL_SCROP);

        bottomView = findViewById(R.id.ch_view_item_experience_bottom_view);
        bottomView.setVisibility(GONE);

        clickView = findViewById(R.id.ch_view_item_experience_vp_click_view);
        clickView.setOnClickListener(this);
    }

    public void setHomeCoursePageTopViewClickListener(HomeCoursePageTopViewClickListener listener) {
        this.listener = listener;
    }

    public void release() {
        if (playerImg != null) {
            playerImg.setImageDrawable(null);
        }

        if (jzCommonPlayer != null) {
            jzCommonPlayer.cancelProgressTimer();
            if (jzCommonPlayer.thumbImageView != null) {
                jzCommonPlayer.thumbImageView.setImageDrawable(null);
            }
        }
    }

    public void setData(HomeSubjectBeanInterface courseBean, int bannerType) {
        if (courseBean != null && titleNameTv != null) {
            setTitleName(courseBean.getTypeName());
            setDiscountName(courseBean.getDiscountName());
            setClassOpenDate(courseBean.getOpenCourseTime());
            setPoint(courseBean.getSalePointName());
            setPriceUnit(courseBean.getUnits());
            setPriceNew(courseBean.getCardPrice());
            setPriceOld(courseBean.getCardPromotionPrice());

            switch (bannerType) {
                case banner_type_video:
                    // 设置视频
                    playerImg.setVisibility(VISIBLE);
                    bottomView.setVisibility(GONE);
                    HomeSubjectExperienceBannerBean bannerBean = courseBean.getBannerVideoBean();
                    if (bannerBean != null) {
                        CommonLogger.e("HomeCoursePageTopView", "铺设图片或者视频--视频---有数据     位置===" + position);
                        jzCommonPlayer.setVisibility(VISIBLE);
                        setVideoPlay(bannerBean.getBg_img_url_video(), bannerBean.getBg_img_url());
                    } else {
                        CommonLogger.e("HomeCoursePageTopView", "铺设图片或者视频--视频---数据为null   位置===" + position);
                        jzCommonPlayer.setVisibility(GONE);
                        setVideoPlay("", "");
                    }
                    break;

                case banner_type_image:
                default:
                    List<HomeSubjectExperienceBannerBean> imgList = courseBean.getBannerImgList();
                    CommonLogger.e("HomeCoursePageTopView", "铺设图片或者视频---图片--数量==="
                            + (imgList != null ? imgList.size() : 0) +
                            "    位置===" + position);

                    if (bottomView != null) {
                        jzCommonPlayer.setVisibility(GONE);
                        playerImg.setVisibility(GONE);

                        bottomView.setVisibility(VISIBLE);
                        bottomView.setDataForObject(imgList);
                    }
                    break;
            }
        }
    }

    /**
     * 是否可以播放视频（和图片轮播互斥）
     *
     * @return true 可以  false  不可以
     */
    public boolean isCanPlayVideo() {
        return jzCommonPlayer != null && jzCommonPlayer.getVisibility() == VISIBLE && !StringUtils.isNullOrEmpty(mVideoUrl);
    }

    public void startVideo() {
        if (isCanPlayVideo()) {
            jzCommonPlayer.startVideo();
            CommonLogger.e("HomeCursePageTopView", "视频播放生命周期---startVideo    位置===" + position);
        }
    }

    private void setVideoPlay(String videoUrl, String imgUrl) {
        if (jzCommonPlayer != null) {
            this.mVideoUrl = videoUrl;
            if (!StringUtils.isNullOrEmpty(mVideoUrl)) {
                jzCommonPlayer.onStatePreparing();
                jzCommonPlayer.setUp(videoUrl, "", 1);
                CommonLogger.e("HomeCoursePageTopView", "铺设图片或者视频---视频---数据设置播放     位置===" + position);
            }
        }
        if (playerImg != null) {
            CommonLogger.e("HomeCoursePageTopView", "铺设视频--封面----地址===" + imgUrl + "    位置===" + position);
            ImageLoadingUtil.INSTANCE.loadImg(this, imgUrl, R.drawable.common_banner_default, R.drawable.common_default_big, playerImg);
        }
    }

    /**
     * 是否可以 图片执行动效（和播放视频互斥）
     *
     * @return
     */
    public boolean isCanPlayImg() {
        return bottomView != null && bottomView.getVisibility() == VISIBLE && bottomView.isCanPlay();
    }


    public void startPlayImg() {
        if (isCanPlayImg()) {
            bottomView.startAutoPlay();
        }
    }

    public void stopPlayImg() {
        if (isCanPlayImg()) {
            bottomView.stopAutoPlay();
        }
    }

    public void setTitleName(String titleName) {
        if (titleNameTv != null) {
            titleNameTv.setText(StringUtils.getFiltedNullStr(titleName));
        }
    }

    public void setDiscountName(String discountName) {
        if (discountNameTv != null) {
            discountNameTv.setText(StringUtils.getFiltedNullStr(discountName));
        }
    }

    public void setClassOpenDate(String classOpenDate) {
        if (classOpenDateTv != null) {
            classOpenDateTv.setText(new StringBuilder(StringUtils.getFiltedNullStr(classOpenDate)).append("开课"));
        }
    }

    public void setPoint(String point) {
        if (pointTv != null) {
            pointTv.setText(StringUtils.getFiltedNullStr(point));
        }
    }

    public void setPriceUnit(String priceUnit) {
        if (priceUnitTv != null) {
            priceUnitTv.setText(StringUtils.getFiltedNullStr(priceUnit));
        }
    }

    public void setPriceNew(String priceNew) {
        if (priceNewTv != null) {
            priceNewTv.setText(StringUtils.getFiltedNullStr(priceNew));
        }
    }

    public void setPriceOld(String priceOld) {
        if (priceOldTv != null) {
            priceOldTv.setText(StringUtils.getFiltedNullStr(priceOld));
        }
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onTopViewClick(v);
        }
    }

    public void showThumbImg() {
        CommonLogger.e("HomeCoursePageTopView", "图片控制---showThumbImg---开始===" + playerImg.getAlpha() + "   位置===" + position);

        animationHideCancel();

        if (playerImg != null && playerImg.getAlpha() < 1.0f) {
            playerImg.setAlpha(1.0f);
            CommonLogger.e("HomeCoursePageTopView", "图片控制---showThumbImg---执行    位置===" + position);
        }
    }

    private void animationImgShow() {
        CommonLogger.e("HomeCoursePageTopView", "图片控制———animationImgShow---开始    位置===" + position);
        if (playerImg != null && playerImg.getAlpha() == 0f) {
            CommonLogger.e("HomeCoursePageTopView", "图片控制———animationImgShow---开始    执行===" + position);
            AnimationUtil.alphaAnimation(playerImg, 0f, 1.0f, ANIMATION_DURATION);
        }
    }

    private void animationHideCancel() {
        if (animatorHide != null && animatorHide.isRunning()) {
            animatorHide.cancel();
        }
    }

    private void animationImgHide() {
        CommonLogger.e("HomeCoursePageTopView", "图片控制———animationImgHide__开始     位置===" + position);
        if (playerImg != null && playerImg.getAlpha() == 1.0f) {
            CommonLogger.e("HomeCoursePageTopView", "图片控制———animationImgHide__执行     位置===" + position);
            if (animatorHide == null) {
                animatorHide = AnimationUtil.alphaAnimation(playerImg, 1.0f, 0f, ANIMATION_DURATION);
            } else {
                animatorHide.start();
            }
        }
    }

    @Override
    public void onPrepared() {

    }

    @Override
    public void onStatePreparing() {

    }

    @Override
    public void onStatePlaying() {
        animationImgHide();
    }

    @Override
    public void onStateAutoComplete() {
        animationImgShow();
    }

    @Override
    public void onStateError() {
        animationImgShow();
    }

    @Override
    public void onReset() {

    }

    private int position = -1;

    public void setPosition(int position) {
        this.position = position;
    }
}
