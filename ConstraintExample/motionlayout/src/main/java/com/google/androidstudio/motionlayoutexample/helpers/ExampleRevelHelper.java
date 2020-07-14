/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.androidstudio.motionlayoutexample.helpers;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.BounceInterpolator;

import androidx.constraintlayout.widget.Barrier;
import androidx.constraintlayout.widget.ConstraintHelper;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Guideline;

import com.ao.libandroidutils.DLog;


public class ExampleRevelHelper extends ConstraintHelper {
    protected ConstraintLayout mContainer;
    private int mComputedCenterX,mComputedCenterY;

    public ExampleRevelHelper(Context context) {
        super(context);
    }

    public ExampleRevelHelper(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ExampleRevelHelper(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void updatePostLayout(ConstraintLayout container) {
        super.updatePostLayout(container);

        DLog.ee( "updatePostLayout x="+mComputedCenterX+" y="+mComputedCenterY+" w="+getWidth()+" h="+getHeight());
        DLog.ee( "updatePostLayout w="+container.getWidth()+" h="+container.getHeight());
    }

    @Override
    public void updatePostMeasure(ConstraintLayout container) {
        super.updatePostMeasure(container);
        DLog.ee( "POSTMeasure x="+mComputedCenterX+" y="+mComputedCenterY+" w="+getWidth()+" h="+getHeight());
        DLog.ee( "POSTMeasure w="+container.getWidth()+" h="+container.getHeight());
    }

    /**
     * @param container
     * @hide
     */
    @Override
    public void updatePreLayout(ConstraintLayout container) {
        super.updatePreLayout(container);
        float minX=-1, maxX=-1, minY=-1, maxY=-1;
        for (View view : getViews(container)) {
            if( minX == -1 || minX > view.getX() ) minX = view.getX();
            if( minY == -1 || minY > view.getY() ) minY = view.getY();
            if( maxX == -1 || maxX < view.getX() + view.getWidth() ) maxX = view.getX() + view.getWidth();
            if( maxY == -1 || maxY < view.getY() + view.getHeight() ) maxY = view.getY()+ view.getHeight();
        }

        if (mContainer!=container) {
            int rad = (int) Math.hypot( maxX - mComputedCenterX, maxY - mComputedCenterY );
            Animator anim =
                    ViewAnimationUtils.createCircularReveal( this,
                            (int) mComputedCenterX - getLeft(),
                            (int) mComputedCenterY - getTop(),
                            0, rad);
            anim.setDuration(2000);
            anim.start();
        }
        mContainer = container;
        mComputedCenterX = (int) (( maxX + minX ) /2);
        mComputedCenterY = (int) ((maxY + minY ) / 2);
        DLog.ee( "updatePreLayout x="+mComputedCenterX+" y="+mComputedCenterY+" w="+getWidth()+" h="+getHeight());
        DLog.ee( "updatePreLayout w="+container.getWidth()+" h="+container.getHeight());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
