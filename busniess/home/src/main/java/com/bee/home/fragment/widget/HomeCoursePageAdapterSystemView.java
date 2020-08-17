package com.bee.home.fragment.widget;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.callback.NavCallback;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bee.android.common.utils.AnimationUtil;
import com.bee.android.common.web.config.H5WebConfig;
import com.bee.core.logger.CommonLogger;
import com.bee.core.utils.CommonUtil;
import com.bee.core.utils.StringUtils;
import com.bee.home.R;
import com.bee.home.fragment.bean.HomeSubjectSystemBean;
import com.bee.home.fragment.listener.HomeCoursePageTopViewClickListener;
import com.bee.home.fragment.listener.HomeCourseScrollViewInfoListener;

import org.jetbrains.annotations.NotNull;

/**
 * @Description: 首页--系统课View
 */
public class HomeCoursePageAdapterSystemView extends ConstraintLayout implements
        HomeCoursePageTopViewClickListener,
        View.OnClickListener,
        HomeCourseScrollViewInfoListener {

    private Context context;

    private HomeCoursePageTopView homeCoursePageTopView;
    private TextView subButtonTv; // "立即订阅"

    private HomeSubjectSystemBean courseBean;

    private String subjectName;

    boolean isClick = false;

    public HomeCoursePageAdapterSystemView(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public HomeCoursePageAdapterSystemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }

    public HomeCoursePageAdapterSystemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView();
    }

    private void initView() {
        View.inflate(context, R.layout.home_fragment_view_home_system, this);

        homeCoursePageTopView = findViewById(R.id.home_course_page_system_top_view);
        homeCoursePageTopView.setHomeCoursePageTopViewClickListener(this);

        subButtonTv = findViewById(R.id.home_course_page_system_subscribe_tv);
        setOnClickListener(this);
    }

    public void setData(HomeSubjectSystemBean courseBean, String subjectName) {
        if (courseBean != null
                && !StringUtils.isNullOrEmpty(courseBean.getCourse_type())
                && homeCoursePageTopView != null) {
            this.courseBean = courseBean;
            this.subjectName = subjectName;
            setVisibility(VISIBLE);

            homeCoursePageTopView.setPosition(position);
            homeCoursePageTopView.setData(courseBean, courseBean.getBannerType());

            subButtonTv.setText(StringUtils.getFiltedNullStr(courseBean.getButton_name()));
        } else {
            setVisibility(GONE);
        }
    }

    @Override
    public void onTopViewClick(@NotNull View view) {
        onClick(view);
    }

    @Override
    public void onClick(View v) {
        if (courseBean != null) {
            if (isClick) {
                return;
            }
            isClick = true;
            if (CommonUtil.isFastClick()) {
                return;
            }

            CommonLogger.e("系统课点击", "ARouter跳转之前");
            AnimationUtil.scaleAnimation(context, this);
            ARouter.getInstance().build(H5WebConfig.H5_ROUTER_WEB_ACTIVITY)
                    .withString(H5WebConfig.H5_PARAM_FLAG, H5WebConfig.H5_FLAG_SYSTEM_COURSE)
                    .withString(H5WebConfig.H5_PARAM_URL, courseBean.getWeb_url())
                    .navigation(context, new NavCallback() {
                        @Override
                        public void onArrival(Postcard postcard) {
                            new Handler().postDelayed(() -> {
                                isClick = false;
                            }, 500);
                        }
                    });
            CommonLogger.e("系统课点击", "ARouter跳转之后");
        }
    }


    @Override
    public int getTopY() {
        int[] location = new int[2];
        getLocationOnScreen(location);
        return location[1];
    }

    @Override
    public int getBottomY() {
        int[] location = new int[2];
        getLocationOnScreen(location);

        int y = location[1];
        int height = getHeight();
        return y + height;
    }

    @Override
    public int onGetHeight() {
        return getHeight();
    }

    @Override
    public boolean isCanPlayVideo() {
        if (homeCoursePageTopView != null) {
            return homeCoursePageTopView.isCanPlayVideo();
        }
        return false;
    }

    @Override
    public boolean isCanPlayImg() {
        if (homeCoursePageTopView != null) {
            return homeCoursePageTopView.isCanPlayImg();
        }
        return false;
    }

    @Override
    public void startVideo() {
        if (homeCoursePageTopView != null) {
            homeCoursePageTopView.startVideo();
        }
    }

    @Override
    public void showThumbImg() {
        if (homeCoursePageTopView != null) {
            homeCoursePageTopView.showThumbImg();
        }
    }

    @Override
    public void startPlayImg() {
        if (homeCoursePageTopView != null) {
            homeCoursePageTopView.startPlayImg();
        }
    }

    @Override
    public void stopPlayImg() {
        if (homeCoursePageTopView != null) {
            homeCoursePageTopView.stopPlayImg();
        }
    }

    private int position = -1;

    public void setPosition(int position) {
        this.position = position;
    }
}
