package com.bee.home.fragment.widget;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

import com.bee.core.logger.CommonLogger;
import com.bee.home.fragment.listener.HomeJzCommonPlayerListener;

import cn.jzvd.JZDataSource;
import cn.jzvd.JzvdStd;

/**
 * @Description: 首页课程卡片播放器
 */
public class HomeJzCommonPlayer extends JzvdStd {

    private HomeJzCommonPlayerListener listener;

    public HomeJzCommonPlayer(Context context) {
        super(context);
    }

    public HomeJzCommonPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setHomeJzCommonPlayerListener(HomeJzCommonPlayerListener listener) {
        this.listener = listener;
    }

    @Override
    public void updateStartImage() {
//        super.updateStartImage();
    }

    @Override
    public void onPrepared() {
        super.onPrepared();

        mediaInterface.setVolume(0f, 0f);
        if (listener != null) {
            listener.onPrepared();
        }
    }


    @Override
    public void onStatePreparing() {
        super.onStatePreparing();
        if (listener != null) {
            listener.onStatePreparing();
        }
    }

    @Override
    public void onStatePlaying() {
        super.onStatePlaying();
        if (listener != null) {
            listener.onStatePlaying();
        }
    }

    @Override
    public void onStateAutoComplete() {
        super.onStateAutoComplete();
    }

    @Override
    public void onStateError() {
        super.onStateError();
        if (listener != null) {
            listener.onStateError();
        }
    }

    @Override
    public void reset() {
        if (listener != null) {
            listener.onReset();
        }
        super.reset();
    }

    @Override
    public void setAllControlsVisiblity(int topCon, int bottomCon, int startBtn, int loadingPro, int thumbImg, int bottomPro, int retryLayout) {
        super.setAllControlsVisiblity(View.INVISIBLE, View.INVISIBLE, View.INVISIBLE, View.INVISIBLE, View.GONE, View.INVISIBLE, View.INVISIBLE);
    }

    public void setUp(String url, String title, int screen) {
        setUp(new JZDataSource(url, title), screen);

        titleTextView.setVisibility(View.INVISIBLE);
        if (mDialogProgressBar != null) {
            mDialogProgressBar.setVisibility(View.INVISIBLE);
        }
        if (mDialogBrightnessProgressBar != null) {
            mDialogBrightnessProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void setUp(JZDataSource jzDataSource, int screen) {
        super.setUp(jzDataSource, screen);
        titleTextView.setVisibility(View.INVISIBLE);
        if (mDialogProgressBar != null) {
            mDialogProgressBar.setVisibility(View.INVISIBLE);
        }
        if (mDialogBrightnessProgressBar != null) {
            mDialogBrightnessProgressBar.setVisibility(View.INVISIBLE);
        }

        if (batteryLevel != null) {
            batteryLevel.setVisibility(View.INVISIBLE);
        }

        if (backButton != null) {
            backButton.setVisibility(View.INVISIBLE);
        }

        if (bottomProgressBar != null) {
            bottomProgressBar.setVisibility(View.INVISIBLE);
        }

        if (topContainer != null) {
            topContainer.setVisibility(View.INVISIBLE);
        }
        if (bottomContainer != null) {
            bottomContainer.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onAutoCompletion() {
        if (listener != null) {
            listener.onStateAutoComplete();
        }
        super.onAutoCompletion();
        CommonLogger.e("HomeCoursePageTopView", "视频播放自动循环播放——准备==" + state);
        new Handler().postDelayed(() -> {
            CommonLogger.w("HomeCoursePageTopView", "视频播放自动循环——开始——执行前==" + state);
            if (state == STATE_AUTO_COMPLETE) {
                startVideo();
                CommonLogger.e("HomeCoursePageTopView", "视屏播放自动循环——开始——执行后==" + state);
            }
        }, 2000);
    }
}
