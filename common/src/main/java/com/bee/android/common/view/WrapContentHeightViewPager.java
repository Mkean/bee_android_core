package com.bee.android.common.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

/**
 * @Description:
 */
public class WrapContentHeightViewPager extends ViewPager {

    public WrapContentHeightViewPager(@NonNull Context context) {
        super(context);
    }

    public WrapContentHeightViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int height = 0;

        // 下面遍历所有child的高度
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.measure(widthMeasureSpec,
                    getChildMeasureSpec(heightMeasureSpec,
                            0, child.getLayoutParams().height)); // getChildMeasureSpec 获取到 child 具体的高度

            int h = child.getMeasuredHeight();
            // 采用最大的view的高度

            if (h > height) {
                height = h;
            }
        }
        setMeasuredDimension(getMeasuredWidth(), height);
    }
}
