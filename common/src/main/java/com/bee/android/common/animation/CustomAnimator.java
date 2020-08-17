package com.bee.android.common.animation;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;

import com.chad.library.adapter.base.animation.BaseAnimation;

/**
 * @Description:
 */
public class CustomAnimator implements BaseAnimation {
    @Override
    public Animator[] getAnimators(View view) {
        return new Animator[]{
                ObjectAnimator.ofFloat(view,"scaleY",1,1.02f,1),
                ObjectAnimator.ofFloat(view,"scaleX",1,1.02f,1)
        };
    }
}
