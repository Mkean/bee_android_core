package com.bee.android.common.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import com.bee.android.common.R
import kotlinx.android.synthetic.main.common_my_loading_layout.view.*

class MyLoadingView : FrameLayout {

    private lateinit var mRotate: Animation

    constructor(context: Context) : this(context, null) {}

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0) {}

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context);
    }

    private fun init(context: Context) {
        LayoutInflater.from(context).inflate(R.layout.common_my_loading_layout, this, true)
        mRotate = AnimationUtils.loadAnimation(context, R.anim.common_my_loading_rotate_anim);
    }

    fun showLoading() {
        visibility = VISIBLE
        loading_image.startAnimation(mRotate)
    }

    fun hideLoading() {
        visibility = GONE
        loading_image.clearAnimation()
    }
}