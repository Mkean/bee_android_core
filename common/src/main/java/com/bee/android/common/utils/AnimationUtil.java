package com.bee.android.common.utils;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bee.android.common.R;

/**
 * @Description:
 */
public class AnimationUtil {

    /**
     * ScaleAnimation
     * 从左下 缩放到 右上 的动画
     *
     * @param duration
     * @return
     */
    public static ScaleAnimation setScaleAnimationForLeftBottom(long duration) {
        ScaleAnimation animation = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1);
        animation.setDuration(duration);
        animation.setFillAfter(true);
        return animation;
    }

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

    /**
     * 属性动画
     * 位移动画
     *
     * @param view
     * @param startX
     * @param endX
     * @param duration
     */
    public static void trainsAnimation(View view, float startX, float endX, long duration) {
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(view, "translationX", startX, startX + endX);
        animatorX.setDuration(duration);
        animatorX.start();
    }

    /**
     * 属性动画
     * 缩小动画
     *
     * @param view
     * @param duration
     */
    public static void scaleAnimationSmall(View view, long duration) {
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 0f);
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(duration);
        animatorSet.setInterpolator(new DecelerateInterpolator());
        animatorSet.play(animatorY).with(animatorX);
        animatorSet.start();
    }

    /**
     * 属性动画
     * 放大动画
     *
     * @param view
     * @param duration
     */
    public static void scaleAnimationBig(View view, long duration) {
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(view, "scaleY", 0f, 1f);
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(view, "scaleX", 0f, 1f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(duration);
        animatorSet.setInterpolator(new DecelerateInterpolator());
        animatorSet.play(animatorY).with(animatorX);
        animatorSet.start();
    }
}
