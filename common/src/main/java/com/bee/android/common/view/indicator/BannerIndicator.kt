package com.bee.android.common.view.indicator

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.viewpager.widget.ViewPager
import com.bee.android.common.R
import com.bee.core.utils.ScreenUtil

/**
 *@Description:
 */
class BannerIndicator : LinearLayout {

    companion object {
        val TAG: String = "BannerIndicator"
    }

    private var viewPager: ViewPager? = null
    private var smallSet: AnimatorSet? = null
    private var largeSet: AnimatorSet? = null

    private var dsahGap: Int = 0
    private var slider_width: Int = 0
    private var slider_height: Int = 0
    private var sliderAlign: Int = 0

    private var mSelectColor: Int = 0
    private var mUnSelectColor: Int = 0

    var temoLeavePosition: Int = 0
    private var count: Int = 0;

    constructor(context: Context?) : this(context, null)

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        orientation = HORIZONTAL
        if (context != null) {
            val array = context.obtainStyledAttributes(attrs, R.styleable.CommonBannerIndicator, defStyleAttr, 0)
            dsahGap = array.getDimension(R.styleable.CommonBannerIndicator_common_gap, ScreenUtil.dip2px(context, 2f).toFloat()).toInt()
            slider_width = array.getDimension(R.styleable.CommonBannerIndicator_common_slider_width, ScreenUtil.dip2px(context, 5f).toFloat()).toInt()
            slider_height = array.getDimension(R.styleable.CommonBannerIndicator_common_slider_height, ScreenUtil.dip2px(context, 2f).toFloat()).toInt()
            sliderAlign = array.getInt(R.styleable.CommonBannerIndicator_common_slider_align, 1)

            mSelectColor = array.getColor(R.styleable.CommonBannerIndicator_common_select_color, Color.parseColor("#ffc700"))
            mUnSelectColor = array.getColor(R.styleable.CommonBannerIndicator_common_unSelect_color, Color.parseColor("#d4d4d4"))

            array.recycle()
        }

    }

    /**
     * 和ViewPager联动，根据ViewPager页面动态生成相应的小圆点
     */
    fun setUpWithViewPager(viewPager: ViewPager) {
        removeAllViews()

        if (viewPager.adapter == null || viewPager.adapter!!.count < 2) {
            return
        }
        count = viewPager.adapter!!.count - 2

        this.viewPager = viewPager
        for (i: Int in 0..viewPager.adapter!!.count - 2) {
            val imageView = BannerItemView(context, sliderAlign)
            val layoutParams = LayoutParams(slider_width, slider_height)
            // 设置小圆点之间的距离
            if (i > 0) {
                layoutParams.setMargins(dsahGap, 0, 0, 0)
                imageView.alpha = 1f
            } else {
                layoutParams.setMargins(0, 0, 0, 0)
                imageView.alpha = 1f
            }
            imageView.layoutParams = layoutParams
            addView(imageView)

            // 默认第一次指示器增大
            setLarge(0)
        }

        // 根据ViewPager页面切换事件，设置指示器大小变化
        viewPager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                val realPosition = toRealPosition(position)
                resetSize(temoLeavePosition, realPosition)
                temoLeavePosition = realPosition
            }
        })

    }

    fun toRealPosition(position: Int): Int {
        var realPosition = (position - 1) % count
        if (realPosition < 0) {
            realPosition += count
        }
        return realPosition
    }

    /**
     * 重置指示器样式
     *
     * @param last
     * @param current
     */
    private fun resetSize(last: Int, current: Int) {
        if (current < 0) return
        if (current > viewPager!!.adapter!!.count - 2) return
        if (last == current) return
        if (largeSet != null && largeSet!!.isRunning) {
            largeSet!!.end()
        }
        if (smallSet != null && smallSet!!.isRunning) {
            smallSet!!.end()
        }
        setLarge(current)
        setSmall(last)

    }

    /**
     *指示器增大同时设置透明度变化
     *
     *@param current
     */
    fun setLarge(current: Int) {
        if (getChildAt(current) is BannerItemView) {
            val view = getChildAt(current) as BannerItemView
            if (view != null) {
                val paint: Paint? = view.mPaint
                paint?.color = mSelectColor
                view.invalidate()

                largeSet = AnimatorSet()
                val animator = getEnlarge((getChildAt(current) as BannerItemView))
                largeSet!!.play(animator)
                largeSet!!.duration = 618
                largeSet!!.start()
            }
        }
    }

    /**
     * 缩小动画同时设置透明度变化
     *
     * @param small
     */
    fun setSmall(small: Int) {
        if (getChildAt(small) is BannerItemView) {
            val view = getChildAt(small) as BannerItemView
            if (view != null) {
                val paint: Paint? = view.mPaint
                paint?.color = mUnSelectColor
                view.invalidate()

                smallSet = AnimatorSet()
                val animator = getShrink(getChildAt(small) as BannerItemView)
                smallSet!!.play(animator)
                smallSet!!.duration = 618
                smallSet!!.start()
            }
        }
    }

    /**
     * 放大动画
     *
     * @param roundRectView
     */
    private fun getEnlarge(roundRectView: BannerItemView): ValueAnimator =
            ObjectAnimator.ofFloat(roundRectView, "rectWidth", 0f, getOffset(roundRectView).toFloat())

    /**
     * 缩小动画
     *
     * @param roundRectView
     */
    private fun getShrink(roundRectView: BannerItemView): ValueAnimator =
            ObjectAnimator.ofFloat(roundRectView, "rectWidth", getOffset(roundRectView).toFloat(), 0f)


    /**
     * 根据大小变化方向获取指示器大小偏移量
     *
     * @param roundRectView
     */
    private fun getOffset(roundRectView: BannerItemView): Int {
        var offset = 0
        when (roundRectView.location) {
            0 -> offset = (slider_width - slider_height) / 2
            1, 2 -> offset = slider_width - slider_height
        }
        return offset
    }
}