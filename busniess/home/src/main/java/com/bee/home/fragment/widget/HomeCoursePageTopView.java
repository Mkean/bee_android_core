package com.bee.home.fragment.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.bee.home.R;
import com.bee.home.fragment.listener.HomeCoursePageTopViewClickListener;
import com.bee.home.fragment.listener.HomeJzCommonPlayerListener;
import com.bee.home.fragment.listener.HomeSubjectBeanInterface;

import cn.jzvd.Jzvd;

/**
 * @Description: 首页--体验课和系统课通用 头部View
 */
public class HomeCoursePageTopView extends ConstraintLayout implements HomeJzCommonPlayerListener, View.OnClickListener {

    private final long ANIMATION_DURATION = 1000;

    private Context context;

    private TextView titleNameTv; // "体验课"
    private TextView discountNameTv; // "限时赠送249元代金券"
    private TextView classOpenDataTv; // "9月9日开课"
    private TextView pointTv; // "适合幼小衔接"
    private TextView priceUnitTv; // "两周"
    private TextView priceNewTv;  // "9.9"
    private TextView priceOldTv; // "145"

    private ImageView playerImg; // 视屏封面

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
        classOpenDataTv = findViewById(R.id.home_course_class_open_date_tv);
        pointTv = findViewById(R.id.home_course_experience_point_tv);
        pointTv.setVisibility(GONE);

        priceUnitTv = findViewById(R.id.home_course_price_unit_tv);
        priceNewTv = findViewById(R.id.home_course_price_new_tv);
        priceOldTv = findViewById(R.id.ch_view_item_price_old_tv);
        priceOldTv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

        playerImg = findViewById(R.id.ch_view_item_Experience_show_vp_iv);
        playerImg.setVisibility(GONE);

        jzCommonPlayer = findViewById(R.id.ch_view_item_experience_show_vp);
        jzCommonPlayer.setVisibility(GONE);
        jzCommonPlayer.setHomeJzCommonPlayerListener(this);
        // 根据自己情况选择一个 填充
        Jzvd.setVideoImageDisplayType(Jzvd.VIDEO_IMAGE_DISPLAY_TYPE_FILL_SCROP);

        bottomView = findViewById(R.id.ch_view_item_experience_bottom_view);
        bottomView.setVisibility(GONE);

        clickView = findViewById(R.id.ch_view_item_Experience_vp_click_view);
        clickView.setOnClickListener(this);

    }

    public void setHomeCoursePageTopViewClickListener(HomeCoursePageTopViewClickListener listener) {
        this.listener = listener;
    }

    public void onRelease() {
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
        if(courseBean != null && titleNameTv != null){

        }

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onPrepared() {

    }

    @Override
    public void onStatePreparing() {

    }

    @Override
    public void onStatePlaying() {

    }

    @Override
    public void onStateAutoComplete() {

    }

    @Override
    public void onStateError() {

    }

    @Override
    public void onReset() {

    }
}
