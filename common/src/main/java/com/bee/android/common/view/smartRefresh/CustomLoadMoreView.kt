package com.bee.android.common.view.smartRefresh

import com.bee.android.common.R
import com.chad.library.adapter.base.loadmore.LoadMoreView

/**
 * 自定义加载更多布局
 */
class CustomLoadMoreView : LoadMoreView() {
    override fun getLayoutId(): Int {
        return R.layout.common_view_load_more
    }

    override fun getLoadingViewId(): Int {
        return R.id.load_more_loading_view
    }

    override fun getLoadFailViewId(): Int {
        return R.id.load_more_load_fail_view
    }

    override fun getLoadEndViewId(): Int {
        return R.id.load_more_load_end_view
    }
}