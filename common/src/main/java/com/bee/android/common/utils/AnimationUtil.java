package com.bee.android.common.utils;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.NonNull;

import com.bee.android.common.R;

/**
 * @Description:
 */
public class AnimationUtil {

    public static void scaleAnimation(@NonNull final Context context, @NonNull View view) {
        Animation animation1 = AnimationUtils.loadAnimation(context, R.anim.common_scale_enlarge);
        Animation animation2 = AnimationUtils.loadAnimation(context, R.anim.common_scale_narrow_down);

        AnimationSet set = new AnimationSet(true);
        set.addAnimation(animation1);
        set.addAnimation(animation2);
        set.setInterpolator(new DecelerateInterpolator());
        view.startAnimation(set);

    }

    /**
     * 属性动画
     * 透明度动画
     *
     * @param view
     * @param fromAlpha
     * @param toAlpha
     * @param duration
     * @return
     */
    public static ObjectAnimator alphaAnimation(View view, float fromAlpha, float toAlpha, long duration) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "alpha", fromAlpha, toAlpha);
        objectAnimator.setDuration(duration);
        objectAnimator.start();

        return objectAnimator;
    }
}
