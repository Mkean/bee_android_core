package com.bee.android.common.view.pageState;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.L;
import com.bee.android.common.R;

import org.jetbrains.annotations.NotNull;

/**
 * 页面状态切换管理布局
 * <p>
 * 原则上APP应该有公共的状态切换页面，本类提供加载中、各种错误、空、需要登录、内容布局状态的默认布局，
 * 本Layout默认给所有子Page设置Clickable=true防止点击传穿透
 * 不同界面背景是否透明会导致UI和Content重叠需要自行变更处理
 * 注意contentView必须只能有一个，请包裹使用
 */
public class PageStateRelativeLayout extends RelativeLayout implements IPageStateChange {
    protected int emptyResId, errorNetResId, errorServerResId, loadingResId, loginResId;

    protected int errorServerClickResId, errorNetClickResId;

    protected String loadingHint, emptyHint, errorNetHint, errorServerHint;

    public static final String DEFAULT_LOADING_HINT = "加载中...";
    protected int childViewPaddingTop, childViewPaddingBottom = 0;
    protected int loadingBgColor, emptyBgColor, errorNetBgColor, errorServerBgColor, loginBgColor;

    protected boolean isChildClickable = true;
    protected ViewGroup contentLayout;

    protected int curPageState = PAGE_STATE_LOADING;
    protected View curExtraPageView;

    public static final int PAGE_STATE_CONTENT = 1;
    public static final int PAGE_STATE_ERROR_NET = 2;
    public static final int PAGE_STATE_ERROR_SERVER = 3;
    public static final int PAGE_STATE_EMPTY = 4;
    public static final int PAGE_STATE_LOADING = 5;
    public static final int PAGE_STATE_LOGIN = 6;

    public static final int LOADING_TYPE_DEFAULT = 1;

    protected int loadingType;

    protected LayoutInflater layoutInflater;

    public PageStateRelativeLayout(Context context) {
        this(context, null);
    }

    public PageStateRelativeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PageStateRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        layoutInflater = LayoutInflater.from(getContext());
        initAttrs(context, attrs, defStyleAttr);
    }

    protected void initAttrs(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.PageStateRelativeLayout, 0, 0);
        try {
            emptyResId = typedArray.getResourceId(R.styleable.PageStateRelativeLayout_emptyLayoutId, R.layout.common_view_page_state_empty_default);
            errorNetResId = typedArray.getResourceId(R.styleable.PageStateRelativeLayout_errorNetLayoutId, R.layout.common_view_page_state_error_net_default);
            errorServerResId = typedArray.getResourceId(R.styleable.PageStateRelativeLayout_errorServerLayoutId, R.layout.common_view_page_state_error_server_default);
            loadingResId = typedArray.getResourceId(R.styleable.PageStateRelativeLayout_loadingLayoutId, R.layout.common_view_page_state_loading_default);

            errorServerClickResId = typedArray.getResourceId(R.styleable.PageStateRelativeLayout_errorServerClickId, R.id.rl_container);
            errorNetClickResId = typedArray.getResourceId(R.styleable.PageStateRelativeLayout_errorNetClickId, R.id.rl_container);

            loadingType = typedArray.getInt(R.styleable.PageStateRelativeLayout_loadingType, LOADING_TYPE_DEFAULT);
            loadingHint = typedArray.getString(R.styleable.PageStateRelativeLayout_loadingHint);
            emptyHint = typedArray.getString(R.styleable.PageStateRelativeLayout_emptyHint);
            errorNetHint = typedArray.getString(R.styleable.PageStateRelativeLayout_errorNetHint);
            errorServerHint = typedArray.getString(R.styleable.PageStateRelativeLayout_errorServerHint);

            int defaultPageColor = getResources().getColor(R.color.common_default_bg_color);

            loadingBgColor = typedArray.getColor(R.styleable.PageStateRelativeLayout_loadingLayoutBg, defaultPageColor);
            emptyBgColor = typedArray.getColor(R.styleable.PageStateRelativeLayout_emptyLayoutBg, defaultPageColor);
            errorNetBgColor = typedArray.getColor(R.styleable.PageStateRelativeLayout_errorNetLayoutBg, defaultPageColor);
            errorServerBgColor = typedArray.getColor(R.styleable.PageStateRelativeLayout_errorServerLayoutBg, defaultPageColor);
            loginBgColor = typedArray.getColor(R.styleable.PageStateRelativeLayout_loginLayoutBg, defaultPageColor);

            childViewPaddingTop = typedArray.getDimensionPixelSize(R.styleable.PageStateRelativeLayout_childViewPaddingTop, 0);
            childViewPaddingBottom = typedArray.getDimensionPixelSize(R.styleable.PageStateRelativeLayout_childViewPaddingBottom, 0);
            isChildClickable = typedArray.getBoolean(R.styleable.PageStateRelativeLayout_isChildClickable, true);
            if (TextUtils.isEmpty(loadingHint)) {
                loadingHint = DEFAULT_LOADING_HINT;
            }
        } finally {
            typedArray.recycle();
        }
    }

    public interface OnPageStateChangeListener {
        void onPageStateChanged(int pageSate);
    }

    public interface OnStatePageClickListener {

        void onErrorNetStateClick();

        void onErrorServerStateClick();
    }

    protected OnPageStateChangeListener onPageStateChangeListener;
    protected OnStatePageClickListener onStatePageClickListener;

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        onContentViewAdded();
    }

    public void onContentViewAdded() {
        int childCount = getChildCount();
        if (childCount != 1) {
            return;
        }
        contentLayout = (ViewGroup) getChildAt(0);
        if (isChildClickable) {
            contentLayout.setClickable(true);
        }
    }

    public ViewGroup getContentLayout() {
        return contentLayout;
    }

    public void setContentView(int layoutResId) {
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        setContentView(layoutResId, layoutParams);
    }

    public void setContentView(int layoutResId, LayoutParams layoutParams) {
        ViewGroup view = (ViewGroup) layoutInflater.inflate(layoutResId, this, false);
        setContentView(view, layoutParams);
    }

    public void setContentView(ViewGroup view) {
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        setContentView(view, layoutParams);
    }

    public void setContentView(ViewGroup view, LayoutParams layoutParams) {
        boolean isHasContentView = contentLayout != null;
        if (view == contentLayout) {
            return;
        }
        addView(view, 0, layoutParams);
        if (isHasContentView) {
            View oldContent = getChildAt(1);
            removeView(oldContent);
        }
        contentLayout = view;
        onContentViewAdded();
    }

    public void setOnPageStateChangeListener(OnPageStateChangeListener onPageStateChangeListener) {
        this.onPageStateChangeListener = onPageStateChangeListener;
    }

    public void setOnStatePageClickListener(OnStatePageClickListener onStatePageClickListener) {
        this.onStatePageClickListener = onStatePageClickListener;
    }

    @Override
    public void showContent() {
        showPage(PAGE_STATE_CONTENT);
    }

    @Override
    public void showError(PageError error) {
        int errCode = error.getErrCode();
        if (errCode == PageError.ERROR_CODE_NETWORK) {
            showErrorNet();
        } else if (errCode == PageError.ERROR_CODE_SERVER) {
            showErrorServer();
        } else if (errCode == PageError.ERROR_CODE_CODE) {
            showErrorCode();
        }
    }

    public void showErrorServer() {
        showError(PAGE_STATE_ERROR_SERVER);
    }

    public void showErrorNet() {
        showError(PAGE_STATE_ERROR_NET);
    }

    public void showErrorCode() {
        showError(PAGE_STATE_ERROR_SERVER);
    }

    public void showError(int errorState) {
        showPage(errorState);
    }

    @Override
    public void showEmpty() {
        showPage(PAGE_STATE_EMPTY);
    }

    @Override
    public void showLoading(int loadType) {
        this.loadingType = loadType;
        showPage(PAGE_STATE_LOADING);
    }

    @Override
    public void showLogin() {
        showPage(PAGE_STATE_LOGIN);
    }

    protected void showPage(int pageState) {
        int childCount = getChildCount();
//        if (childCount < 1 || contentLayout == null) {
//            return;
//        }
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child != contentLayout) {
                removeView(child);
            }
        }
        curPageState = pageState;
        curExtraPageView = handPageByState(pageState);
        if (onPageStateChangeListener != null) {
            onPageStateChangeListener.onPageStateChanged(pageState);
        }
    }

    protected View handPageByState(int pageState) {
        View extraView = null;
        boolean contentVisible = true;
        switch (pageState) {
            case PAGE_STATE_ERROR_NET:
                ViewGroup errorNetLayout = getErrorNetLayout();
                if (errorNetLayout != null) {
                    addView(errorNetLayout);
                    contentVisible = false;
                    extraView = errorNetLayout;
                }
                break;
            case PAGE_STATE_ERROR_SERVER:
                ViewGroup errorServerLayout = getErrorServerLayout();
                if (errorServerLayout != null) {
                    addView(errorServerLayout);
                    contentVisible = false;
                    extraView = errorServerLayout;
                }
                break;
            case PAGE_STATE_EMPTY:
                ViewGroup emptyLayout = getEmptyLayout();
                if (emptyLayout != null) {
                    addView(emptyLayout);
                    contentVisible = false;
                    extraView = emptyLayout;
                }
                break;
            case PAGE_STATE_LOGIN:
                ViewGroup loginLayout = getLoginLayout();
                if (loginLayout != null) {
                    addView(loginLayout);
                    contentVisible = false;
                    extraView = loginLayout;
                }
                break;
            case PAGE_STATE_LOADING:
                ViewGroup loadingLayout = getLoadingLayout();
                if (loadingLayout != null) {
                    addView(loadingLayout);
                    contentVisible = false;
                    extraView = loadingLayout;
                }
                break;
            default:
                extraView = null;
        }
        if (contentLayout != null) {
            contentLayout.setVisibility(contentVisible ? VISIBLE : INVISIBLE);
        }
        //todo 如果有动画关闭动画
        if (extraView != null && isChildClickable) {
            extraView.setClickable(true);
        }
        return extraView;
    }

    protected ViewGroup getErrorNetLayout() {
        if (errorNetResId > 0) {
            ViewGroup errorViewGroup = (ViewGroup) layoutInflater.inflate(errorNetResId, this, false);
            if (errorNetResId == R.layout.common_view_page_state_error_net_default) {
                if (errorNetBgColor > 0) {
                    errorViewGroup.setBackgroundColor(errorNetBgColor);
                }
                if (!TextUtils.isEmpty(errorNetHint)) {
                    TextView errorHintTextView = errorViewGroup.findViewById(R.id.tv_msg);
                    errorHintTextView.setText(errorNetHint);
                }
            }
            View retryView = errorViewGroup.findViewById(errorNetClickResId);
            if (retryView != null) {
                retryView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onStatePageClickListener != null) {
                            onStatePageClickListener.onErrorNetStateClick();
                        }
                    }
                });
            }
            errorViewGroup.setPadding(0, childViewPaddingTop, 0, childViewPaddingBottom);
            return errorViewGroup;
        }
        return null;
    }

    protected ViewGroup getErrorServerLayout() {
        if (errorServerResId > 0) {
            ViewGroup errorViewGroup = (ViewGroup) layoutInflater.inflate(errorServerResId, this, false);
            if (errorServerResId == R.layout.common_view_page_state_error_server_default) {
                if (errorServerBgColor > 0) {
                    errorViewGroup.setBackgroundColor(errorServerBgColor);
                }
                if (!TextUtils.isEmpty(errorServerHint)) {
                    TextView errorHintTextView = errorViewGroup.findViewById(R.id.tv_msg);
                    errorHintTextView.setText(errorServerHint);
                }
            }
            View retryView = errorViewGroup.findViewById(errorServerClickResId);
            if (retryView != null) {
                retryView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onStatePageClickListener != null) {
                            onStatePageClickListener.onErrorServerStateClick();
                        }
                    }
                });
            }
            errorViewGroup.setPadding(0, childViewPaddingTop, 0, childViewPaddingBottom);
            return errorViewGroup;
        }
        return null;
    }

    protected ViewGroup getEmptyLayout() {
        if (emptyResId > 0) {
            ViewGroup emptyViewGroup = (ViewGroup) layoutInflater.inflate(emptyResId, this, false);
            if (emptyResId == R.layout.common_view_page_state_empty_default) {
                if (emptyBgColor > 0) {
                    emptyViewGroup.setBackgroundColor(emptyBgColor);
                }
                if (!TextUtils.isEmpty(emptyHint)) {
                    TextView emptyHintTextView = emptyViewGroup.findViewById(R.id.tv_msg);
                    emptyHintTextView.setText(emptyHint);
                }
            }
            emptyViewGroup.setPadding(0, childViewPaddingTop, 0, childViewPaddingBottom);
            return emptyViewGroup;
        }
        return null;
    }

    protected ViewGroup getLoginLayout() {
        if (emptyResId > 0) {
            ViewGroup loginViewGroup = (ViewGroup) layoutInflater.inflate(loginResId, this, false);
            if (loginResId == R.layout.common_view_page_state_login_default) {
                if (loginBgColor > 0) {
                    loginViewGroup.setBackgroundColor(loginBgColor);
                }
            }
            loginViewGroup.setPadding(0, childViewPaddingTop, 0, childViewPaddingBottom);
            return loginViewGroup;
        }
        return null;
    }

    protected ViewGroup getLoadingLayout() {
        ViewGroup loadingViewGroup = null;
        if (loadingResId > 0) {
            loadingViewGroup = (ViewGroup) layoutInflater.inflate(loadingResId, this, false);
            loadingViewGroup.setPadding(0, childViewPaddingTop, 0, childViewPaddingBottom);
            if (loadingResId == R.layout.common_view_page_state_loading_default) {
                TextView loadingTextView = loadingViewGroup.findViewById(R.id.tv_msg);
                loadingTextView.setText(loadingHint);
                //todo 启动相关动画
            }
        }
        return loadingViewGroup;
    }

    public int getCurPageState() {
        return curPageState;
    }

    public View getCurExtraPageView() {
        return curExtraPageView;
    }

    public void setEmptyResId(int emptyResId) {
        this.emptyResId = emptyResId;
    }

    public void setLoadingResId(int loadingResId) {
        this.loadingResId = loadingResId;
    }

    public void setErrorNetResId(int errorNetResId) {
        this.errorNetResId = errorNetResId;
    }

    public void setErrorServerResId(int errorServerResId) {
        this.errorServerResId = errorServerResId;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        //todo  启动相关动画
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        //todo 关闭相关动画
    }


}
