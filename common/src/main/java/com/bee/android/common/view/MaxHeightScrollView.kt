package com.bee.android.common.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.widget.ScrollView
import com.bee.android.common.R
import kotlin.math.max

/**
 *@Description:
 */
class MaxHeightScrollView : ScrollView {

    var maxHeight: Int;

    constructor(context: Context?) : this(context, null)

    constructor(context: Context?, attrs: AttributeSet?) : this(context, null, 0)

    @SuppressLint("Recycle", "CustomViewStyleable")
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {

        val typedArray = context?.obtainStyledAttributes(attrs, R.styleable.Common_MaxHeightScrollView)

        maxHeight = typedArray?.getDimensionPixelSize(R.styleable.Common_MaxHeightScrollView_common_max_height,
                0)!!

        typedArray.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (maxHeight > 0) {
            super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(maxHeight, MeasureSpec.AT_MOST))
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }
}