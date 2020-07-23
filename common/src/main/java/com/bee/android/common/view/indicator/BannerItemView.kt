package com.bee.android.common.view.indicator

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

/**
 *@Description:
 */
class BannerItemView : View {

    companion object {
        const val TAG: String = "BannerItemView"
        const val CENTER: Int = 0
        const val LEFT: Int = 1
        const val RIGHT: Int = 2
    }

     var mPaint: Paint? = null
    private var rectF: RectF? = null
    private var rectWidth: Float = 0f
        set(value) {
            field = value
            invalidate()
        }
    var location: Int = CENTER


    constructor(context: Context?) : this(context, null)

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context?, location: Int) : this(context) {
        this.location = location
    }

    private fun init() {
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        rectF = RectF()
        mPaint!!.color = Color.parseColor("d4d4d4")
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        when (location) {
            CENTER -> {
                // 中间对齐
                rectF!!.left = width / 2 - height / 2 - rectWidth
                rectF!!.right = width / 2 + height / 2 + rectWidth
                rectF!!.top = 0f
                rectF!!.bottom = height.toFloat()
            }
            LEFT -> {
                rectF!!.left = width - height - rectWidth
                rectF!!.right = width.toFloat()
                rectF!!.top = 0f
                rectF!!.bottom = height.toFloat()
            }
            RIGHT -> {
                rectF!!.left = 0f
                rectF!!.right = height + rectWidth
                rectF!!.top = 0f
                rectF!!.bottom = height.toFloat()
            }
        }
        canvas!!.drawRoundRect(rectF!!, height / 2.toFloat(), height / 2.toFloat(), mPaint!!)
    }
}