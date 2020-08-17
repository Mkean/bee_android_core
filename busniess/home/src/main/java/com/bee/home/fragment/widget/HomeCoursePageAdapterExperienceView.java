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
import com.bee.core.utils.TimeUtil;
import com.bee.home.R;
import com.bee.home.fragment.bean.HomeEventBusExperienceTimeZero;
import com.bee.home.fragment.bean.HomeSubjectExperienceBean;
import com.bee.home.fragment.listener.HomeCoursePageTopViewClickListener;
import com.bee.home.fragment.listener.HomeCourseScrollViewInfoListener;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

import static com.bee.home.fragment.bean.HomeSubjectExperienceBean.SALE_STATE_END;
import static com.bee.home.fragment.bean.HomeSubjectExperienceBean.SALE_STATE_START;

/**
 * @Description: 首页--体验课
 */
public class HomeCoursePageAdapterExperienceView extends ConstraintLayout implements View.OnClickListener,
        TimeShowLayout.CountdownTimerZeroListener,
        HomeCourseScrollViewInfoListener,
        HomeCoursePageTopViewClickListener {

    private Context context;

    private HomeCoursePageTopView homeCoursePageTopView;

    private TextView classShopOpenDateTv; // "9月9号"
    private TextView classShopOpenTimeTv; // "12:00"
    private TextView classShopOpenEndTv; // "开售"

    private TextView subButtonTv; // "立即订阅"

    private TextView bottomOneTv;
    private TextView bottomTwoTv;
    private TextView bottomThreeTv;

    private TimeShowLayout timeShowLayout; // 倒计时： "01：35：24"
    private TextView timeHintTv; // "距离开售"

    private TextView saleStateNumTv;

    private HeaderAndTextLayout headerAndTextLayout;
    private HomeSubjectExperienceBean courseBean;

    private String subjectName;
    private boolean isClick = false;

    public HomeCoursePageAdapterExperienceView(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public HomeCoursePageAdapterExperienceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }

    public HomeCoursePageAdapterExperienceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView();
    }

    private void initView() {
        View.inflate(context, R.layout.home_fragment_view_home_experiencce, this);

        homeCoursePageTopView = findViewById(R.id.home_course_page_top_view);
        homeCoursePageTopView.setHomeCoursePageTopViewClickListener(this);

        classShopOpenDateTv = findViewById(R.id.home_course_page_class_open_date_tv);
        classShopOpenTimeTv = findViewById(R.id.home_course_page_class_open_time_tv);
        classShopOpenEndTv = findViewById(R.id.home_course_page_class_open_end_tv);

        subButtonTv = findViewById(R.id.home_course_page_subscribe_tv);

        bottomOneTv = findViewById(R.id.home_course_page_class_left_num_one_tv);
        bottomTwoTv = findViewById(R.id.home_course_page_class_left_num_two_tv);
        bottomThreeTv = findViewById(R.id.home_course_page_class_left_num_three_tv);

        timeShowLayout = findViewById(R.id.home_course_page_time_show);
        timeHintTv = findViewById(R.id.home_course_page_distance_sale_tv);

        saleStateNumTv = findViewById(R.id.home_course_page_sale_state_tv);

        headerAndTextLayout = findViewById(R.id.home_course_page_header_layout);

        stockStateShow(3);
        setOnClickListener(this);
    }

    public void setData(HomeSubjectExperienceBean courseBean, String subjectName) {
        if (courseBean != null
                && !StringUtils.isNullOrEmpty(courseBean.getCourse_type())
                && homeCoursePageTopView != null) {
            this.courseBean = courseBean;
            this.subjectName = subjectName;
            setVisibility(VISIBLE);
            homeCoursePageTopView.setPosition(position);
            homeCoursePageTopView.setData(courseBean, courseBean.getBannerType());
            // 状态
            stockStateShow(courseBean.getSaleState());
        } else {
            setVisibility(GONE);
        }
    }

    private void allBottomViewGone() {
        if (classShopOpenDateTv != null) {
            classShopOpenDateTv.setVisibility(INVISIBLE);
            classShopOpenTimeTv.setVisibility(INVISIBLE);
            classShopOpenEndTv.setVisibility(INVISIBLE);
            subButtonTv.setVisibility(INVISIBLE);
            timeShowLayout.setVisibility(INVISIBLE);
            timeHintTv.setVisibility(INVISIBLE);
            saleStateNumTv.setVisibility(INVISIBLE);
            headerAndTextLayout.setVisibility(INVISIBLE);
            bottomOneTv.setVisibility(INVISIBLE);
            bottomTwoTv.setVisibility(INVISIBLE);
            bottomThreeTv.setVisibility(INVISIBLE);
        }
    }

    private void stockStateShow(int state) {
        if (courseBean != null && classShopOpenDateTv != null) {
            allBottomViewGone();

            long startTime = courseBean.getSaleStartTime();
            long serverTime = courseBean.getServerTime();
            switch (state) {
                case SALE_STATE_END:
                    // 已售完
                    // 展示 下次销售的售卖时间

                    showOpenShopTime(startTime, serverTime);

                    saleStateNumTv.setVisibility(VISIBLE);
                    String saleState = courseBean.getSale_state_name();
                    String num = "，剩余" + courseBean.getSale_rest_num() + "/" + courseBean.getShow_stock() + "份";
                    saleStateNumTv.setText(saleState + num);

                    headerAndTextLayout.setData(null);

                    break;
                case SALE_STATE_START:
                    // 已开售
                    // 展示剩余数量和抢购

                    bottomOneTv.setVisibility(VISIBLE);
                    bottomTwoTv.setVisibility(VISIBLE);
                    bottomThreeTv.setVisibility(VISIBLE);
                    subButtonTv.setVisibility(VISIBLE);

                    bottomOneTv.setText("剩余");
                    bottomTwoTv.setText(StringUtils.getFiltedNullStr(courseBean.getSale_rest_num()));
                    bottomThreeTv.setText("/" + courseBean.getShow_stock() + "·份");

                    subButtonTv.setText(StringUtils.getFiltedNullStr(courseBean.getButton_name()));

                    headerAndTextLayout.setData(courseBean.getSale_user_list());
                    break;
                default:
                    // 未开售
                    // 时间文字展示
                    showOpenShopTime(startTime, serverTime);
                    // 倒计时展示
                    timeShow(startTime, serverTime);

                    headerAndTextLayout.setData(null);
                    break;
            }
        }
    }

    private void showOpenShopTime(long startTime, long serverTime) {
        if (serverTime > 0 && startTime > 0 && classShopOpenDateTv != null) {
            classShopOpenDateTv.setVisibility(VISIBLE);
            classShopOpenTimeTv.setVisibility(VISIBLE);
            classShopOpenEndTv.setVisibility(VISIBLE);

            // 日期
            if (TimeUtil.isSameDay(startTime, serverTime)) {
                // 如果是同一天
                classShopOpenDateTv.setText("今天");
            } else if (TimeUtil.isYesterday(startTime, serverTime)) {
                // 如果是明天
                classShopOpenDateTv.setText("明天");
            } else {
                // 都不是，则展示 几月几号
                classShopOpenDateTv.setText(TimeUtil.longToString(startTime, TimeUtil.FORMAT_MM_DD));
            }
            // 时间
            classShopOpenTimeTv.setText(TimeUtil.longToString(startTime, TimeUtil.FORMAT_HH_MM));
            classShopOpenEndTv.setText("开售");
        }
    }

    /**
     * 倒计时展示
     *
     * @param startTime
     * @param serverTime
     */
    private void timeShow(long startTime, long serverTime) {
        if (serverTime > 0 && startTime > 0 && courseBean != null && timeShowLayout != null) {
            timeShowLayout.setVisibility(VISIBLE);
            timeHintTv.setVisibility(VISIBLE);

            timeShowLayout.initTime(startTime - serverTime, this);
            timeShowLayout.startTime();
        }
    }

    @Override
    public void onTimeZero() {
        // 倒计时完毕
        CommonLogger.e("首页", "体验课倒计时结束发送===" + subjectName);

        EventBus.getDefault().post(new HomeEventBusExperienceTimeZero());
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
            CommonLogger.e("体验课点击", "ARouter跳转之前");
            AnimationUtil.scaleAnimation(context, this);
            ARouter.getInstance().build(H5WebConfig.H5_ROUTER_WEB_ACTIVITY)
                    .withString(H5WebConfig.H5_PARAM_FLAG, H5WebConfig.H5_FLAG_EXPERIENCE_COURSE)
                    .withString(H5WebConfig.H5_PARAM_URL, courseBean.getWeb_url())
                    .navigation(context, new NavCallback() {
                        @Override
                        public void onArrival(Postcard postcard) {
                            new Handler().postDelayed(() -> {
                                isClick = false;
                            }, 500);
                        }
                    });
            CommonLogger.e("体验课点击", "ARouter跳转之后");
        }
    }

    public void stopHeaderAndTextLayout() {
        if (headerAndTextLayout != null) {
            headerAndTextLayout.stopAutoPlay();
        }
    }

    public void startHeaderAndTextLayout() {
        if (headerAndTextLayout != null) {
            headerAndTextLayout.startAutoPLay();
        }
    }

    public void destroy() {
        stopHeaderAndTextLayout();
        if (timeShowLayout != null) {
            timeShowLayout.onDestroy();
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
