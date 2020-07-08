package com.bee.android.common.view.shadow;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Outline;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.RelativeLayout;

import com.bee.android.common.R;

/**
 * 可用于裁剪圆角 RelativeLayout 父布局
 */
public class ShadowClipRelativeLayout extends RelativeLayout {
    private float radius;
    private float shadowAlpha;

    public ShadowClipRelativeLayout(Context context) {
        super(context);
        init(context, null, 0);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        if (null != attrs || defStyleAttr != 0) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.common_shadowRl, defStyleAttr, 0);
            radius = ta.getDimension(R.styleable.common_shadowRl_shadowRadius, 4f);
            shadowAlpha = ta.getFloat(R.styleable.common_shadowRl_shadowAlpha, 0.55f);
            ta.recycle();
        }
        setShadow(radius, shadowAlpha);
    }

    @SuppressLint("NewApi")
    public void setShadow(final float radius, final float shadowAlpha) {
        setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                int top = 0, bottom = view.getHeight(),
                        left = 0, right = view.getWidth();
                outline.setRoundRect(left, top,
                        right, bottom, radius);
                outline.setAlpha(shadowAlpha);
            }
        });
        setClipToOutline(true);
        invalidate();
    }


    public ShadowClipRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public ShadowClipRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }
}
