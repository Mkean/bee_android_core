package com.bee.home.fragment.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.Nullable;

/**
 * @Description: 动态改变View宽高的TextView
 */
@SuppressLint("AppCompatCustomView")
public class AnimationTextView extends TextView {

    public AnimationTextView(Context context) {
        super(context);
    }

    public AnimationTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AnimationTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setAniWidth(int width) {
        getLayoutParams().width = width;
        requestLayout(); // 必须调用，否则宽度改变但UI没有刷新
    }

    public int getAniWidth() {
        return getLayoutParams().width;
    }

    public void setAniHeight(int height) {
        getLayoutParams().height = height;
        requestLayout();
    }

    public int getAniHeight() {
        return getLayoutParams().height;
    }
}
