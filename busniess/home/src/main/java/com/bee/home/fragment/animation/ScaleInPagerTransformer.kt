package com.bee.home.fragment.animation

import android.view.View
import androidx.viewpager.widget.ViewPager

/**
 *@Description: 首页--轮播图 item ，Viewpager 的进入进出动画View
 *
 */
class ScaleInPagerTransformer() : ViewPager.PageTransformer {

    companion object {
        const val DEFAULT_MIN_SCALE = 0.85f
    }

    var mMinScale = DEFAULT_MIN_SCALE


    override fun transformPage(view: View, position: Float) {
        val pageWidth = view.width
        val pageHeight = view.height

        view.pivotY = (pageHeight / 2).toFloat()
        view.pivotX = (pageWidth / 2).toFloat()

        if (position < -1) { // [-Infinity, -1 )
            // This page is way off-screen to the left.
            view.scaleX = mMinScale
            view.scaleY = mMinScale
            view.pivotX = pageWidth.toFloat()
        } else if (position <= 1) { // [-1, 1]
            // Modify the default slide transition to shrink the page as well
            if (position < 0) { // 1-2: 1 [0,-1]; 2-1: 1[-1,0]
                val scaleFactor = (1 + position) * (1 - mMinScale) + mMinScale

                view.scaleX = scaleFactor
                view.scaleY = scaleFactor
                view.pivotX = pageWidth * (0.5f * -position)
            } else { // 1-2: 2 [1,0]; 2-1: 2 [0,1]
                val scaleFactor = (1 - position) * (1 - mMinScale) + mMinScale;

                view.scaleX = scaleFactor
                view.scaleY = scaleFactor
                view.pivotX = pageWidth * ((1 - position) * 0.5f)
            }

        } else { // (1, +Infinity]
            view.pivotX = 0f
            view.scaleX = mMinScale
            view.scaleY = mMinScale
        }
    }

}