package com.blackspider.util;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.animation.AnimationUtils;
import android.widget.ViewFlipper;

/**
 * Created by USER on 3/12/2018.
 */

public class SwipeGestureDetector extends GestureDetector.SimpleOnGestureListener {
    public Context mContext;
    private ViewFlipper mViewFlipper;

    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;

    public SwipeGestureDetector(Context context, ViewFlipper viewFlipper){
        mContext = context;
        mViewFlipper = viewFlipper;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        try {
            // right to left swipe
            if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                mViewFlipper.setInAnimation(AnimationUtils.loadAnimation(mContext, android.R.anim.slide_in_left));
                mViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(mContext, android.R.anim.slide_out_right));
                mViewFlipper.showNext();
                return true;
            } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                mViewFlipper.setInAnimation(AnimationUtils.loadAnimation(mContext, android.R.anim.slide_out_right));
                mViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(mContext, android.R.anim.slide_in_left));
                mViewFlipper.showPrevious();
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
