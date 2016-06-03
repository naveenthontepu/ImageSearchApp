package com.naveenthontepu.imagesearchapp;

import android.animation.Animator;
import android.view.View;

/**
 * Created by naveen thontepu on 02-06-2016.
 */
public class AnimatorEndListener implements Animator.AnimatorListener {
    int visibility;
    View view;

    public AnimatorEndListener(int visibility, View view) {
        this.visibility = visibility;
        this.view = view;
    }

    @Override
    public void onAnimationStart(Animator animation) {

    }

    @Override
    public void onAnimationEnd(Animator animation) {

    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }
}
