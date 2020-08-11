package com.bee.home.fragment.utils;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bee.core.logger.CommonLogger;
import com.bee.home.fragment.fragment.HomeCourseFragment;
import com.bee.home.fragment.listener.HomeCourseScrollViewInfoListener;

import cn.jzvd.Jzvd;

/**
 * @Description: 课程卡片---滚动监听类：控制视频和图片轮播
 */
public class HomeCourseScrollUtil {

    private static final String TAG = "滚动监听---HomeCourseScrollUtil";

    private volatile static HomeCourseScrollUtil INSTANCE = null;

    private HomeCourseScrollListener scrollListener;
    private HomeCourseFragment mCourseFragment;

    private HomeCourseScrollViewInfoListener actionPlayView;

    private int lineY;

    private HomeCourseScrollUtil() {
    }

    public static HomeCourseScrollUtil getInstance() {
        if (INSTANCE == null) {
            synchronized (HomeCourseScrollUtil.class) {
                if (INSTANCE == null) {
                    INSTANCE = new HomeCourseScrollUtil();
                }
            }
        }
        return INSTANCE;
    }

    public void setLineY(int y) {
        CommonLogger.e(TAG, "设置基准线===" + y);
        lineY = y;
    }

    public int getLineY() {
        return lineY;
    }

    public void register(HomeCourseFragment courseFragment) {
        CommonLogger.e(TAG, "register");

        if (mCourseFragment == null || !mCourseFragment.equals(courseFragment)) {
            mCourseFragment = null;
            releasePlayAction();
            mCourseFragment = courseFragment;
            videoPlayAction();
        }
    }

    public void videoOnResume() {
        if (actionPlayView != null) {
            Jzvd.goOnPlayOnResume();
        }
    }

    public void videoOnPause() {
        if (actionPlayView != null) {
            Jzvd.goOnPlayOnPause();
        }
    }

    public void releasePlayAction() {
        if (actionPlayView != null) {
            actionPlayView.showThumbImg();
            actionPlayView.stopPlayImg();
        }
        Jzvd.releaseAllVideos();
        actionPlayView = null;
        CommonLogger.e(TAG, "releasePlayAction--停止视频播放和GIF");
    }

    public HomeCourseScrollListener getScrollListener() {
        if (scrollListener == null) {
            scrollListener = new HomeCourseScrollListener();
        }
        return scrollListener;
    }

    public class HomeCourseScrollListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
        }

        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            switch (newState) {
                case RecyclerView.SCROLL_STATE_IDLE: // 滚动停止
                    CommonLogger.e(TAG, "滚动停止");
                    videoPlayAction();
                    break;

                case RecyclerView.SCROLL_STATE_DRAGGING: // 手指拖动
                    break;

                case RecyclerView.SCROLL_STATE_SETTLING: // 惯性滚动
                    break;
            }
        }
    }

    public void videoPlayAction() {
        CommonLogger.e(TAG, "videoPlayAction");
        CommonLogger.e(TAG, "mCourseFragment是否为null==" + (mCourseFragment == null));

        if (mCourseFragment != null) {
            CommonLogger.e(TAG, "getRecyclerView()是否为null==" + (mCourseFragment.getRecyclerView() == null));
            if (mCourseFragment.getRecyclerView() != null) {
                CommonLogger.e(TAG, "getLayoutManager()是否为null==" + (mCourseFragment.getRecyclerView().getLayoutManager() == null));
            }
        }

        if (mCourseFragment != null
                && mCourseFragment.getRecyclerView() != null
                && mCourseFragment.getRecyclerView().getLayoutManager() != null) {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mCourseFragment.getRecyclerView().getLayoutManager();
            if (linearLayoutManager != null) {
                // 获取展示的View 角标
                int firstVisible = linearLayoutManager.findFirstVisibleItemPosition();
                int lastCompletely = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                CommonLogger.e(TAG, "_firstVisible==" + firstVisible);
                CommonLogger.e(TAG, "_lastCompletely==" + lastCompletely);

                int lineY = getLineY();

                CommonLogger.e(TAG, "_lineY==" + lineY);
                // 是否已经调用了播放视频
                boolean isShowVideo = false;
                // 是否已经调用了图片动效
                boolean isShowPlayImg = false;

                // 开始循环展示的View，是否达到播放条件
                for (int i = firstVisible; i <= lastCompletely; i++) {
                    View view = linearLayoutManager.getChildAt(i);
                    // 如果是监听播放的View
                    CommonLogger.e(TAG, "_view instanceof HomeCourseScrollViewInfoListener==" + (view instanceof HomeCourseScrollViewInfoListener));
                    if (view instanceof HomeCourseScrollViewInfoListener) {
                        HomeCourseScrollViewInfoListener infoListener = (HomeCourseScrollViewInfoListener) view;

                        int topY = infoListener.getTopY();
                        int bottomY = infoListener.getBottomY();

                        CommonLogger.e(TAG, "_topY" + topY);
                        CommonLogger.e(TAG, "_bottomY" + bottomY);

                        // 计算是否达到锚点
                        if (topY <= lineY && bottomY >= lineY) {
                            CommonLogger.e(TAG, "到达条件执行条件——锚点==" + i);

                            // 判断是否是同一个对象：如果不是同一个对象，或者 当前View不在屏幕内
                            // 不是同一个对象，才做下面的处理
                            if (actionPlayView == null || !actionPlayView.equals(infoListener)) {
                                CommonLogger.e(TAG, "达到条件执行条件——不是同一个对象");
                                if (infoListener.isCanPlayVideo() && !isShowVideo) {
                                    // 如果是视频播放
                                    releasePlayAction();
                                    actionPlayView = infoListener;
                                    actionPlayView.startVideo();
                                    isShowVideo = true;
                                    CommonLogger.e(TAG, "达到条件执行条件——播放视频");
                                } else if (infoListener.isCanPlayImg() && !isShowPlayImg) {
                                    // 如果是图片动效播放
                                    releasePlayAction();
                                    actionPlayView = infoListener;
                                    actionPlayView.stopPlayImg();
                                    isShowPlayImg = true;
                                    CommonLogger.e(TAG, "达到条件执行条件——播放图片动效");
                                }
                            }
                        } else if (bottomY < 0) {
                            // 超出屏幕
                            CommonLogger.e(TAG, "达到条件执行条件——超出屏幕");

                            // 如果当前播放View超出屏幕，则将当前播放View设置为null
                            if (actionPlayView != null && actionPlayView.equals(infoListener)) {
                                releasePlayAction();
                            }
                        }

                    }
                }
            }
        }
    }
}
