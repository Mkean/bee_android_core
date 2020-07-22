package com.bee.android.common.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.DrawableRes;

import com.bee.android.common.R;
import com.bee.core.utils.ScreenUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 一个方便在多种状态下切换布局
 */
public class MultipleStatusView extends RelativeLayout {
    private static final String TAG = "MultipleStatusView";

    private static final LayoutParams DEFAULT_LAYOUT_PARAMS =
            new LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT);

    public static final int STATUS_CONTENT = 0x00;
    public static final int STATUS_LOADING = 0x01;
    public static final int STATUS_EMPTY = 0x02;
    public static final int STATUS_ERROR = 0x03;
    public static final int STATUS_NO_NETWORK = 0x04;
    public static final int STATUS_LOGIN = 0x05;
    public static final int STATUS_ERROR_FROM_MY = 0x06;
    public static final int STATUS_IMAGE_EMPTY = 0x07;

    private View mEmptyView;
    private View mErrorView;
    private View mLoadingView;
    private View mNoNetworkView;
    private View mLoginView;
    private View mErrorFromMyView;
    private View mImageEmptyView;

    private int mEmptyViewResId;
    private int mErrorViewResId;
    private int mLoadingViewResId;
    private int mNoNetworkViewResId;
    private int mLoginViewResId;
    private int mErrorFromMyViewResId;
    private int mImageEmptyResId;

    private int mViewStatus = -1;
    private View mCurrentView;
    private final LayoutInflater mInflater;
    private OnViewClickListener mOnViewClickListener;
    private OnViewStatusChangeListener mViewStatusListener;

    private Map<View, Integer> mContentViewVisibility = new HashMap();

    public MultipleStatusView(Context context) {
        this(context, null);
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    public MultipleStatusView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultipleStatusView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MultipleStatusView, defStyleAttr, 0);
        mEmptyViewResId = a.getResourceId(R.styleable.MultipleStatusView_emptyView, R.layout.common_empty_view);
        mImageEmptyResId = a.getResourceId(R.styleable.MultipleStatusView_emptyFromWorkWallView, R.layout.common_image_empty_view);
        mErrorViewResId = a.getResourceId(R.styleable.MultipleStatusView_errorView, R.layout.common_error_view);
        mLoadingViewResId = a.getResourceId(R.styleable.MultipleStatusView_loadingView, R.layout.common_loading_view);
        mNoNetworkViewResId = a.getResourceId(R.styleable.MultipleStatusView_noNetworkView, R.layout.common_no_network_view);
        mLoginViewResId = a.getResourceId(R.styleable.MultipleStatusView_loginView, R.layout.common_login_view);
        mErrorFromMyViewResId = a.getResourceId(R.styleable.MultipleStatusView_errorFromMyView, R.layout.common_error_from_my_view);
        a.recycle();
        mInflater = LayoutInflater.from(getContext());
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
//        ViewGroup mViewGroup = (ViewGroup) getParent().getParent();
//        if (null != mViewGroup) {
//            MonkeyLogger.i(TAG, "width:" + mViewGroup.getWidth());
//            MonkeyLogger.i(TAG, "ScreenHeight:" + ScreenUtil.getScreenHeight(MonkeyApplication.app));
//            MonkeyLogger.i(TAG, "height:" + mViewGroup.getHeight());
//            this.getLayoutParams().height=mViewGroup.getHeight();
//        }

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        Log.e("onFinishInflate", "onFinishInflate");
        showContent();
    }

    /**
     * 获取当前状态
     *
     * @return 视图状态
     */
    public int getViewStatus() {
        return mViewStatus;
    }

    /**
     * 设置点击事件
     *
     * @param onViewClickListener 点击事件
     */
    public void setOnViewClickListener(OnViewClickListener onViewClickListener) {
        this.mOnViewClickListener = onViewClickListener;
    }

    /**
     * 显示空视图
     */
    public void showEmpty() {
        showEmpty(mEmptyViewResId, DEFAULT_LAYOUT_PARAMS);
    }

    /**
     * 显示空视图
     *
     * @param hintResId  自定义提示文本内容
     * @param formatArgs 占位符参数
     */
    public void showEmpty(int hintResId, Object... formatArgs) {
        showEmpty();
        setStatusHintContent(mEmptyView, hintResId, formatArgs);
    }

    /**
     * 显示空视图
     *
     * @param hint 自定义提示文本内容
     */
    public void showEmpty(String hint) {
        showEmpty();
        setStatusHintContent(mEmptyView, hint);
    }

    /**
     * 显示空视图
     *
     * @param layoutId     自定义布局文件
     * @param layoutParams 布局参数
     */
    private void showEmpty(int layoutId, ViewGroup.LayoutParams layoutParams) {
        showEmpty(null == mEmptyView ? inflateView(layoutId) : mEmptyView, layoutParams);
    }

    /**
     * 显示空视图
     *
     * @param view         自定义视图
     * @param layoutParams 布局参数
     */
    private void showEmpty(View view, ViewGroup.LayoutParams layoutParams) {
        checkNull(view, "Empty view is null.");
        checkNull(layoutParams, "Layout params is null.");
        changeViewStatus(STATUS_EMPTY);
        if (null == mEmptyView) {
            mEmptyView = view;
            View emptyRetryView = mEmptyView.findViewById(R.id.retry_view);
            if (null != mOnViewClickListener && null != emptyRetryView) {
                emptyRetryView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mOnViewClickListener != null) {
                            mOnViewClickListener.OnRetryClick();
                        }
                    }
                });
            }
            addView(mEmptyView, 0, layoutParams);
        }
        showView(mEmptyView);
    }

    /**
     * 显示空视图
     */
    public void showImageEmpty() {
        showImageEmpty(mImageEmptyResId, DEFAULT_LAYOUT_PARAMS);
    }

    /**
     * 显示空视图
     *
     * @param resId 自定义空页面图片
     */
    public void showImageEmpty(@DrawableRes int resId) {
        showImageEmpty();
        setEmptyImage(mImageEmptyView, resId);
    }

    /**
     * 显示空视图
     *
     * @param resId 自定义空页面图片
     * @param hint  自定义提示文本内容
     */
    public void showImageEmpty(@DrawableRes int resId, String hint) {
        showImageEmpty();
        setStatusHintContent(mImageEmptyView, hint);
        setEmptyImage(mImageEmptyView, resId);
    }

    /**
     * 显示空视图
     *
     * @param hintResId  自定义提示文本内容
     * @param formatArgs 占位符参数
     */
    public void showImageEmpty(int hintResId, Object... formatArgs) {
        showImageEmpty();
        setStatusHintContent(mImageEmptyView, hintResId, formatArgs);
    }

    /**
     * 显示空视图
     *
     * @param hint 自定义提示文本内容
     */
    public void showImageEmpty(String hint) {
        showImageEmpty();
        setStatusHintContent(mImageEmptyView, hint);
    }

    /**
     * 显示空视图
     *
     * @param layoutId     自定义布局文件
     * @param layoutParams 布局参数
     */
    private void showImageEmpty(int layoutId, ViewGroup.LayoutParams layoutParams) {
        showImageEmpty(null == mImageEmptyView ? inflateView(layoutId) : mImageEmptyView, layoutParams);
    }

    /**
     * 显示空视图
     *
     * @param view         自定义视图
     * @param layoutParams 布局参数
     */
    private void showImageEmpty(View view, ViewGroup.LayoutParams layoutParams) {
        checkNull(view, "Empty view is null.");
        checkNull(layoutParams, "Layout params is null.");
        changeViewStatus(STATUS_IMAGE_EMPTY);
        if (null == mImageEmptyView) {
            mImageEmptyView = view;
            View emptyRetryView = mImageEmptyView.findViewById(R.id.retry_view);
            if (null != mOnViewClickListener && null != emptyRetryView) {
                emptyRetryView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mOnViewClickListener != null) {
                            mOnViewClickListener.OnRetryClick();
                        }
                    }
                });
            }
            addView(mImageEmptyView, 0, layoutParams);
        }
        showView(mImageEmptyView);
    }

    /**
     * 显示错误视图
     */
    public void showError() {
        showError(mErrorViewResId, DEFAULT_LAYOUT_PARAMS);
    }

    /**
     * 显示错误视图
     *
     * @param hintResId  自定义提示文本内容
     * @param formatArgs 占位符参数
     */
    public void showError(int hintResId, Object... formatArgs) {
        showError();
        setStatusHintContent(mErrorView, hintResId, formatArgs);
    }

    /**
     * 显示错误视图
     *
     * @param hint 自定义提示文本内容
     */
    public void showError(String hint) {
        showError();
        setStatusHintContent(mErrorView, hint);
    }

    /**
     * 显示错误视图
     *
     * @param layoutId     自定义布局文件
     * @param layoutParams 布局参数
     */
    private void showError(int layoutId, ViewGroup.LayoutParams layoutParams) {
        showError(null == mErrorView ? inflateView(layoutId) : mErrorView, layoutParams);
    }

    /**
     * 显示错误视图
     *
     * @param view         自定义视图
     * @param layoutParams 布局参数
     */
    private final void showError(View view, ViewGroup.LayoutParams layoutParams) {
        checkNull(view, "Error view is null.");
        checkNull(layoutParams, "Layout params is null.");
        changeViewStatus(STATUS_ERROR);
        if (null == mErrorView) {
            mErrorView = view;
            View errorRetryView = mErrorView.findViewById(R.id.retry_view);
            if (null != mOnViewClickListener && null != errorRetryView) {
                errorRetryView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mOnViewClickListener != null) {
                            mOnViewClickListener.OnRetryClick();
                        }
                    }
                });
            }
            addView(mErrorView, 0, layoutParams);
        }
        showView(mErrorView);
    }

    /**
     * 显示错误视图
     */
    public void showErrorFromMy() {
        showErrorFromMy(mErrorFromMyViewResId, DEFAULT_LAYOUT_PARAMS);
    }

    /**
     * 显示错误视图
     *
     * @param hintResId  自定义提示文本内容
     * @param formatArgs 占位符参数
     */
    public void showErrorFromMy(int hintResId, Object... formatArgs) {
        showErrorFromMy();
        setStatusHintContent(mErrorFromMyView, hintResId, formatArgs);
    }

    /**
     * 显示错误视图
     *
     * @param hint 自定义提示文本内容
     */
    public void showErrorFromMy(String hint) {
        showErrorFromMy();
        setStatusHintContent(mErrorFromMyView, hint);
    }

    /**
     * 显示错误视图
     *
     * @param layoutId     自定义布局文件
     * @param layoutParams 布局参数
     */
    private void showErrorFromMy(int layoutId, ViewGroup.LayoutParams layoutParams) {
        showErrorFromMy(null == mErrorFromMyView ? inflateView(layoutId) : mErrorFromMyView, layoutParams);
    }

    /**
     * 显示错误视图
     *
     * @param view         自定义视图
     * @param layoutParams 布局参数
     */
    private final void showErrorFromMy(View view, ViewGroup.LayoutParams layoutParams) {
        checkNull(view, "Error view is null.");
        checkNull(layoutParams, "Layout params is null.");
        changeViewStatus(STATUS_ERROR_FROM_MY);
        if (null == mErrorFromMyView) {
            mErrorFromMyView = view;
            View errorRetryView = mErrorFromMyView.findViewById(R.id.retry_view);
            if (null != mOnViewClickListener && null != errorRetryView) {
                errorRetryView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mOnViewClickListener != null) {
                            mOnViewClickListener.OnRetryClick();
                        }
                    }
                });
            }
            addView(mErrorFromMyView, 0, layoutParams);
        }
        showView(mErrorFromMyView);
    }

    /**
     * 显示加载中视图
     */
    public final void showLoading() {
        showLoading(mLoadingViewResId, DEFAULT_LAYOUT_PARAMS);
    }

    /**
     * 显示加载中视图
     *
     * @param hintResId  自定义提示文本内容
     * @param formatArgs 占位符参数
     */
    public final void showLoading(int hintResId, Object... formatArgs) {
        showLoading();
        setStatusHintContent(mLoadingView, hintResId, formatArgs);
    }

    /**
     * 显示加载中视图
     *
     * @param hint 自定义提示文本内容
     */
    public final void showLoading(String hint) {
        showLoading();
        setStatusHintContent(mLoadingView, hint);
    }

    /**
     * 显示加载中视图
     *
     * @param layoutId     自定义布局文件
     * @param layoutParams 布局参数
     */
    private final void showLoading(int layoutId, ViewGroup.LayoutParams layoutParams) {
        showLoading(null == mLoadingView ? inflateView(layoutId) : mLoadingView, layoutParams);
    }

    /**
     * 显示加载中视图
     *
     * @param view         自定义视图
     * @param layoutParams 布局参数
     */
    private final void showLoading(View view, ViewGroup.LayoutParams layoutParams) {
        checkNull(view, "Loading view is null.");
        checkNull(layoutParams, "Layout params is null.");
        changeViewStatus(STATUS_LOADING);
        if (null == mLoadingView) {
            mLoadingView = view;
            addView(mLoadingView, 0, layoutParams);
        }
        showView(mLoadingView);
    }

    /**
     * 显示登录视图
     */
    public final void showLogin() {
        showLogin(mLoginViewResId, DEFAULT_LAYOUT_PARAMS);
    }

    /**
     * 显示登录视图
     *
     * @param hintResId  自定义提示文本内容
     * @param formatArgs 占位符参数
     */
    public final void showLogin(int hintResId, Object... formatArgs) {
        showLogin();
        setStatusHintContent(mLoginView, hintResId, formatArgs);
    }

    /**
     * 显示登录视图
     *
     * @param hint 自定义提示文本内容
     */
    public final void showLogin(String hint) {
        showLogin();
        setStatusHintContent(mLoginView, hint);
    }

    /**
     * 显示登录视图
     *
     * @param layoutId     自定义布局文件
     * @param layoutParams 布局参数
     */
    private final void showLogin(int layoutId, ViewGroup.LayoutParams layoutParams) {
        showLogin(null == mLoginView ? inflateView(layoutId) : mLoginView, layoutParams);
    }

    /**
     * 显示登录视图
     *
     * @param view         自定义视图
     * @param layoutParams 布局参数
     */
    private final void showLogin(View view, ViewGroup.LayoutParams layoutParams) {
        checkNull(view, "No network view is null.");
        checkNull(layoutParams, "Layout params is null.");
        changeViewStatus(STATUS_LOGIN);
        if (null == mLoginView) {
            mLoginView = view;
            View noNetworkRetryView = mLoginView.findViewById(R.id.retry_view);
            if (null != mOnViewClickListener && null != noNetworkRetryView) {
                noNetworkRetryView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mOnViewClickListener != null) {
                            mOnViewClickListener.OnLoginClick();
                        }
                    }
                });
            }
            addView(mLoginView, 0, layoutParams);
        }
        showView(mLoginView);
    }

    /**
     * 显示无网络视图
     */
    public final void showNoNetwork() {
        showNoNetwork(mNoNetworkViewResId, DEFAULT_LAYOUT_PARAMS);
    }

    /**
     * 显示无网络视图
     *
     * @param hintResId  自定义提示文本内容
     * @param formatArgs 占位符参数
     */
    public final void showNoNetwork(int hintResId, Object... formatArgs) {
        showNoNetwork();
        setStatusHintContent(mNoNetworkView, hintResId, formatArgs);
    }

    /**
     * 显示无网络视图
     *
     * @param hint 自定义提示文本内容
     */
    public final void showNoNetwork(String hint) {
        showNoNetwork();
        setStatusHintContent(mNoNetworkView, hint);
    }

    /**
     * 显示无网络视图
     *
     * @param layoutId     自定义布局文件
     * @param layoutParams 布局参数
     */
    private final void showNoNetwork(int layoutId, ViewGroup.LayoutParams layoutParams) {
        showNoNetwork(null == mNoNetworkView ? inflateView(layoutId) : mNoNetworkView, layoutParams);
    }

    /**
     * 显示无网络视图
     *
     * @param view         自定义视图
     * @param layoutParams 布局参数
     */
    private final void showNoNetwork(View view, ViewGroup.LayoutParams layoutParams) {
        checkNull(view, "No network view is null.");
        checkNull(layoutParams, "Layout params is null.");
        changeViewStatus(STATUS_NO_NETWORK);
        if (null == mNoNetworkView) {
            mNoNetworkView = view;
            View noNetworkRetryView = mNoNetworkView.findViewById(R.id.retry_view);
            if (null != mOnViewClickListener && null != noNetworkRetryView) {
                noNetworkRetryView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mOnViewClickListener != null) {
                            mOnViewClickListener.OnRetryClick();
                        }
                    }
                });
            }
            addView(mNoNetworkView, 0, layoutParams);
        }
        showView(mNoNetworkView);
    }

    /**
     * 显示内容视图
     */
    public final void showContent() {
        changeViewStatus(STATUS_CONTENT);
        showContentView();
    }

    private void setStatusHintContent(View view, int resId, Object... formatArgs) {
        checkNull(view, "Target view is null.");
        setStatusHintContent(view, view.getContext().getString(resId, formatArgs));
    }

    private void setStatusHintContent(View view, String hint) {
        checkNull(view, "Target view is null.");
        TextView hintView = view.findViewById(R.id.hint_content);
        if (null != hintView) {
            hintView.setText(hint);
        } else {
            throw new NullPointerException("Not find the view ID `hint_content`");
        }
    }

    private void setEmptyImage(View view, @DrawableRes int resId) {
        checkNull(view, "Target view is null.");
        ImageView imageView = view.findViewById(R.id.image_view);
        if (null != imageView) {
            imageView.setImageResource(resId);
        } else {
            throw new NullPointerException("Not find the view ID `image_view`");
        }
    }

    private View inflateView(int layoutId) {
        return mInflater.inflate(layoutId, null);
    }

    private void showView(View v) {
        if (v == null) {
            return;
        }
        Log.i(TAG, "showView:" + v.toString());
        if (mCurrentView != null && mCurrentView != v) {
            clear(mCurrentView);
            mCurrentView = null;
        }
        mCurrentView = v;

        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = getChildAt(i);
            if (checkIsContentView(view) && !mContentViewVisibility.containsKey(view)) {
                mContentViewVisibility.put(view, view.getVisibility());
            }
            if (v == mLoadingView) {
                if (view == v) {
                    view.setVisibility(VISIBLE);
                } else {
                    //显示loading态不对内容区域处理，保持之前状态
                }
            } else {
                view.setVisibility(view == v ? View.VISIBLE : View.GONE);
            }
        }
    }

    private void showContentView() {
        Log.e("MultipleStatusView", "showContentView");
        clearAll();
        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = getChildAt(i);
            if (mContentViewVisibility.containsKey(view)) {
                view.setVisibility(mContentViewVisibility.get(view));
            }
        }
    }

    private void checkNull(Object object, String hint) {
        if (null == object) {
            throw new NullPointerException(hint);
        }
    }

    private void clearAll() {
        clear(mEmptyView, mLoadingView, mErrorView, mNoNetworkView, mLoginView, mErrorFromMyView, mImageEmptyView);
        mCurrentView = null;
    }

    private void clear(View... views) {

        if (null == views) {
            return;
        }
        try {
            for (View view : views) {
                if (null != view) {
                    removeView(view);
                    release(view);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void release(View v) {
        if (v == null) {
            return;
        }
        if (v == mEmptyView) {
            mEmptyView = null;
        } else if (v == mErrorView) {
            mErrorView = null;
        } else if (v == mLoadingView) {
            mLoadingView = null;
        } else if (v == mNoNetworkView) {
            mNoNetworkView = null;
        } else if (v == mLoginView) {
            mLoginView = null;
        } else if (v == mErrorFromMyView) {
            mErrorFromMyView = null;
        } else if (v == mImageEmptyView) {
            mImageEmptyView = null;
        }
    }

    private boolean checkIsContentView(View v) {
        if (v == mEmptyView || v == mErrorView || v == mLoadingView
                || v == mNoNetworkView || v == mLoginView || v == mErrorFromMyView || v == mImageEmptyView) {
            return false;
        }
        return true;
    }

    /**
     * 视图点击接口
     */
    public interface OnViewClickListener {

        /**
         * 点击重试View
         */
        void OnRetryClick();

        /**
         * 点击登录View
         */
        void OnLoginClick();
    }

    /**
     * 视图点击接口实现类
     * 考虑到有些界面只需要一个或这两个回调，没有必要全部方法都需要实现，所以提供一个简单的实现类，
     * 想要那个，就重写那个。
     */
    public static class SimpleViewClickListenerImp implements OnViewClickListener {

        @Override
        public void OnRetryClick() {

        }

        @Override
        public void OnLoginClick() {

        }
    }

    /**
     * 视图状态改变接口
     */
    public interface OnViewStatusChangeListener {

        /**
         * 视图状态改变时回调
         *
         * @param oldViewStatus 之前的视图状态
         * @param newViewStatus 新的视图状态
         */
        void onChange(int oldViewStatus, int newViewStatus);
    }

    /**
     * 设置视图状态改变监听事件
     *
     * @param onViewStatusChangeListener 视图状态改变监听事件
     */
    public void setOnViewStatusChangeListener(OnViewStatusChangeListener onViewStatusChangeListener) {
        this.mViewStatusListener = onViewStatusChangeListener;
    }

    /**
     * 改变视图状态
     *
     * @param newViewStatus 新的视图状态
     */
    private void changeViewStatus(int newViewStatus) {
        if (mViewStatus == newViewStatus) {
            return;
        }
        if (null != mViewStatusListener) {
            mViewStatusListener.onChange(mViewStatus, newViewStatus);
        }
        mViewStatus = newViewStatus;
    }

    public void release() {
        clearAll();
        if (null != mOnViewClickListener) {
            mOnViewClickListener = null;
        }
        if (null != mViewStatusListener) {
            mViewStatusListener = null;
        }
    }

    MyLoadingView myLoadingView;

    public void showLoadingView() {
        if (myLoadingView == null) {
            Log.e("MultipleStatusView", "myLoadingView==null");


            myLoadingView = new MyLoadingView(getContext());

            LayoutParams layoutParams = new LayoutParams(ScreenUtil.dip2px(130), ScreenUtil.dip2px(85));
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            myLoadingView.setBackgroundResource(R.drawable.common_shape_70percent_000000_15);
            addView(myLoadingView, layoutParams);
        } else {
            Log.e("MultipleStatusView", "myLoadingView!=null");

            myLoadingView.setVisibility(VISIBLE);

        }
        myLoadingView.showLoading();
    }

    public void hideLoadingView() {
        if (myLoadingView != null) {
            myLoadingView.setVisibility(GONE);
            myLoadingView.hideLoading();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.e("MultipleStatusView", "onDetachedFromWindow");
        if (myLoadingView != null) {
            myLoadingView.clearAnimation();
        }
    }
}
