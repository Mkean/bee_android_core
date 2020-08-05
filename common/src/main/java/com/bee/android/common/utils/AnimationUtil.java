package com.bee.android.common.utils;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.NonNull;

import com.bee.android.common.R;

/**
 * @Description: 动画工具栏，未完成
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
}
