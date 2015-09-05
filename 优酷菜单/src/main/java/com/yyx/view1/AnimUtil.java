package com.yyx.view1;

import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.RelativeLayout;

/**
 * Created by yangyongxin on 15/8/27.
 */
public class AnimUtil {
    public static int animCount = 0;

    public static void closeMeun(RelativeLayout rl, int startOffset) {

        for (int i = 0; i < rl.getChildCount(); i++) {
            rl.getChildAt(i).setEnabled(false);
        }
        RotateAnimation animation = new RotateAnimation(0, -180,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 1);
        animation.setDuration(500);
        animation.setFillAfter(true);
        animation.setStartOffset(startOffset);
        animation.setAnimationListener(new MyAnimationListener());
        rl.startAnimation(animation);
    }

    public static void showMeun(RelativeLayout rl, int startOffset) {
        for (int i = 0; i < rl.getChildCount(); i++) {
            rl.getChildAt(i).setEnabled(true);
        }
        RotateAnimation animation = new RotateAnimation(-180, 0,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 1);
        animation.setDuration(500);
        animation.setFillAfter(true);
        animation.setStartOffset(startOffset);
        animation.setAnimationListener(new MyAnimationListener());
        rl.startAnimation(animation);
    }

    static class MyAnimationListener implements Animation.AnimationListener {

        @Override
        public void onAnimationStart(Animation animation) {
            animCount++;
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            animCount--;
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }
}
