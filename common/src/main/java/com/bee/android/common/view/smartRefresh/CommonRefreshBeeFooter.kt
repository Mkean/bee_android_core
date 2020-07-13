package com.bee.android.common.view.smartRefresh

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import com.bee.android.common.R
import com.scwang.smartrefresh.layout.api.RefreshFooter
import com.scwang.smartrefresh.layout.internal.InternalAbstract

/**
 * 公共蜜蜂样式底部footer
 */
class CommonRefreshBeeFooter : InternalAbstract, RefreshFooter {

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        val view: View = LayoutInflater.from(context).inflate(R.layout.common_view_home_refresh_foot, this, false)
        addView(view)
    }

    override fun setNoMoreData(noMoreData: Boolean): Boolean {
        return false
    }
}