package com.suramire.myapplication.util;

import android.view.View;

import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.view.ViewHelper;

import static com.suramire.myapplication.util.Constant.DELAY;

/**
 * Created by Suramire on 2017/7/26.
 */

public class AnimationUtil {
    public static void show(final View top, final View bottom) {
//        fab.show();
        ValueAnimator animator = ValueAnimator.ofFloat(top.getHeight(), 0).setDuration(DELAY);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                ViewHelper.setTranslationY(top, -value);
            }
        });
        animator.start();
        ValueAnimator animator2 = ValueAnimator.ofFloat(bottom.getHeight(), 0).setDuration(DELAY);
        animator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                ViewHelper.setTranslationY(bottom, value);
            }
        });
        animator2.start();
    }

    public static void hide(final View top, final View bottom) {
        ValueAnimator animator = ValueAnimator.ofFloat(0, top.getHeight()).setDuration(DELAY);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                ViewHelper.setTranslationY(top, -value);
            }
        });
        animator.start();
        ValueAnimator animator2 = ValueAnimator.ofFloat(0, bottom.getHeight()).setDuration(DELAY);
        animator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                ViewHelper.setTranslationY(bottom, value);
            }
        });
        animator2.start();
    }
}
