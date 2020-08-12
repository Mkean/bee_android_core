package com.bee.home.fragment.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Group;

import com.bee.core.manager.countdown.CountdownTimeManager;
import com.bee.core.manager.countdown.CountdownTimeManagerListener;
import com.bee.core.utils.StringUtils;
import com.bee.home.R;

/**
 * @Description: 首页---课程卡片--学科倒计时View
 */
public class TimeShowLayout extends ConstraintLayout implements CountdownTimeManagerListener {

    private final String TAG = this.getClass().getSimpleName();

    // 时间间隔
    private final static int INTERVAL = 1000;

    private final String ZERO_STR = "0";

    private Context context;

    private TextView dayTv;

    private Group timeGroup;

    private TextView hourOneTv;
    private TextView hourTwoTv;

    private TextView minuteOneTv;
    private TextView minuteTwoTv;

    private TextView secondOneTv;
    private TextView secondTwoTv;

    private long totalTime;

    private CountdownTimerZeroListener listener;

    public TimeShowLayout(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public TimeShowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }

    public TimeShowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView();
    }

    private void initView() {
        View.inflate(context, R.layout.home_fragment_view_show_time, this);

        dayTv = findViewById(R.id.time_item_date_tv);
        dayTv.setVisibility(GONE);

        timeGroup = findViewById(R.id.time_group);
        timeGroup.setVisibility(GONE);

        hourOneTv = findViewById(R.id.time_item_hour_one_tv);
        hourTwoTv = findViewById(R.id.time_item_hour_two_tv);

        minuteOneTv = findViewById(R.id.time_item_minute_one_tv);
        minuteTwoTv = findViewById(R.id.time_item_minute_two_tv);

        secondOneTv = findViewById(R.id.time_item_second_one_tv);
        secondTwoTv = findViewById(R.id.time_item_second_two_tv);

        hourOneTv.setTypeface(StringUtils.getTypeface());
        hourTwoTv.setTypeface(StringUtils.getTypeface());
        minuteOneTv.setTypeface(StringUtils.getTypeface());
        minuteTwoTv.setTypeface(StringUtils.getTypeface());
        secondOneTv.setTypeface(StringUtils.getTypeface());
        secondTwoTv.setTypeface(StringUtils.getTypeface());
    }

    @Override
    public void accept(long aLong) {
        totalTime -= 1000; // 减去一秒

        if (totalTime > 0) {
            int day = 0;
            int hour = 0;
            int minute = (int) (totalTime / INTERVAL / 60);
            int second = (int) (totalTime / INTERVAL % 60);
            if (minute > 60) {
                hour = minute / 60;
                minute = minute % 60;
            }
            if (hour >= 24) {
                day = hour / 24;
                hour = hour % 24;
            }
            setCountDownData(day, hour, minute, second);
        } else {
            stopTime();
            setCountDownData(0, 0, 0, 0);
        }
    }

    /**
     * 初始化倒计时
     *
     * @param totalTime 倒计时的总时间
     * @param listener  倒计时结束监听器
     */
    public void initTime(long totalTime, CountdownTimerZeroListener listener) {
        if (totalTime > 0) {
            this.totalTime = totalTime;
            this.listener = listener;
        }
    }

    private void setCountDownData(int day, int hour, int minute, int second) {
        if (day > 0) {
            timeGroup.setVisibility(GONE);
            dayTv.setVisibility(VISIBLE);
            dayTv.setText(StringUtils.getNumberToDINTypeface(getContext(), day + "天"));
        } else {
            timeGroup.setVisibility(VISIBLE);
            dayTv.setVisibility(GONE);
            if (hour > 0) {
                if (hour > 9) {
                    char[] chars = String.valueOf(hour).toCharArray();
                    if (chars.length > 1) {
                        hourOneTv.setText(String.valueOf(chars[0]));
                        hourTwoTv.setText(String.valueOf(chars[1]));
                    }
                } else {
                    hourOneTv.setText(ZERO_STR);
                    hourTwoTv.setText(String.valueOf(hour));
                }
            } else {
                hourOneTv.setText(ZERO_STR);
                hourTwoTv.setText(ZERO_STR);
            }

            if (minute > 0) {
                if (minute > 9) {
                    char[] chars = String.valueOf(minute).toCharArray();
                    if (chars.length > 1) {
                        minuteOneTv.setText(String.valueOf(chars[0]));
                        minuteTwoTv.setText(String.valueOf(chars[1]));
                    }
                } else {
                    minuteOneTv.setText(ZERO_STR);
                    minuteTwoTv.setText(String.valueOf(minute));
                }
            } else {
                minuteOneTv.setText(ZERO_STR);
                minuteTwoTv.setText(ZERO_STR);
            }

            if (second > 0) {
                if (second > 9) {
                    char[] chars = String.valueOf(second).toCharArray();
                    if (chars.length > 1) {
                        secondOneTv.setText(String.valueOf(chars[0]));
                        secondTwoTv.setText(String.valueOf(chars[1]));
                    }
                } else {
                    secondOneTv.setText(ZERO_STR);
                    secondTwoTv.setText(String.valueOf(second));
                }
            } else {
                secondOneTv.setText(ZERO_STR);
                secondTwoTv.setText(ZERO_STR);
            }

            if (listener != null) {
                if (day == 0 && hour == 0 && minute == 0 && second == 0) {
                    listener.onTimeZero();
                }
            }
        }
    }

    public void startTime() {
        if (!CountdownTimeManager.Companion.getInstance().isHasListener(this)) {
            CountdownTimeManager.Companion.getInstance().register(this, 1);
        }
    }

    public void stopTime() {
        if (CountdownTimeManager.Companion.getInstance().isHasListener(this)) {
            CountdownTimeManager.Companion.getInstance().unRegister(this);
        }
    }

    public void onDestroy() {
        this.listener = null;
        stopTime();
    }


    public interface CountdownTimerZeroListener {
        void onTimeZero();
    }
}
