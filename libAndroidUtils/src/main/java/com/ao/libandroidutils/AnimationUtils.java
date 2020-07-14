package com.ao.libandroidutils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.ActivityOptions;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.util.Pair;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AnimationUtils {

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static ActivityOptions getActivityOptions(Activity activity, Pair<View, String>... elements) {
        View decorView = activity.getWindow().getDecorView();
        View statusBackground = decorView.findViewById(android.R.id.statusBarBackground);
        View navBackground = decorView.findViewById(android.R.id.navigationBarBackground);

        List<Pair<View, String>> list = new ArrayList<>(Arrays.asList(elements));
        list.add(Pair.create(statusBackground,
                statusBackground.getTransitionName()));
        if (navBackground != null) list.add(Pair.create(navBackground, navBackground.getTransitionName()));

        final ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(activity, list.toArray(elements));
        return options;
    }

    private static AnimatorSet curAniSet = null;
    static abstract class SwitchDelegate{
        void onBeforeStartSwitchView(View startView, View endView){}
        void onStartSwithView(View startView, View endView, Animator animation){}
        void onEndSwithView(View startView, View endView, Animator animation){}
        void onCancelSwithView(View startView, View endView, Animator animation){}
    }

    private static void switchViewAnimator(final View startView, final View endView, final SwitchDelegate delegate) {
        // If there's an animation in progress, cancel it
        // immediately and proceed with this one.
        if (curAniSet != null) {
            curAniSet.cancel();
        }

        // Load the high-resolution "zoomed-in" image.
        if( delegate != null ) delegate.onBeforeStartSwitchView(startView, endView);
        // Calculate the starting and ending bounds for the zoomed-in image.
        // This step involves lots of math. Yay, math.
        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        // The start bounds are the global visible rectangle of the thumbnail,
        // and the final bounds are the global visible rectangle of the container
        // view. Also set the container view's offset as the origin for the
        // bounds, since that's the origin for the positioning animation
        // properties (X, Y).
        startView.getGlobalVisibleRect(startBounds);
        endView.getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        // Adjust the start bounds to be the same aspect ratio as the final
        // bounds using the "center crop" technique. This prevents undesirable
        // stretching during the animation. Also calculate the start scaling
        // factor (the end scaling factor is always 1.0).
        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            // Extend start bounds horizontally
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            // Extend start bounds vertically
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        startView.setAlpha(0f);
        endView.setVisibility(View.VISIBLE);

        // Set the pivot point for SCALE_X and SCALE_Y transformations
        // to the top-left corner of the zoomed-in view (the default
        // is the center of the view).
        endView.setPivotX(0f);
        endView.setPivotY(0f);

        // Construct and run the parallel animation of the four translation and
        // scale properties (X, Y, SCALE_X, and SCALE_Y).
        AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(endView, View.X,
                        startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(endView, View.Y,
                        startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(endView, View.SCALE_X,
                        startScale, 1f))
                .with(ObjectAnimator.ofFloat(endView,
                        View.SCALE_Y, startScale, 1f));
        set.setDuration(300);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                delegate.onStartSwithView(startView,endView, animation);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                curAniSet = null;
                if( delegate != null ) delegate.onEndSwithView(startView,endView, animation);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                curAniSet = null;
                if( delegate != null ) delegate.onCancelSwithView(startView,endView, animation);
            }
        });
        set.start();
        curAniSet = set;
    }


    /**
     * Get the center of a given view.
     *
     * @param view The view to get coordinates from.
     * @return The center of the given view.
     */
    public static Point getCenterForView(@NonNull View view) {
        final int centerX = (view.getLeft() + view.getRight()) / 2;
        final int centerY = (view.getTop() + view.getBottom()) / 2;
        return new Point(centerX, centerY);
    }

    /**
     * Create a simple circular reveal from a given start view to it's target view.
     * This reveal will start from the start view's boundaries until it fills the target view.
     *
     * @param startView The view to start the reveal from.
     * @param targetView The target view which will be displayed once the reveal is done.
     * @param interpolator The interpolator to use.
     * @return The created circular reveal.
     */
    @NonNull
    public static Animator createCircularReveal(@NonNull View startView, @NonNull View targetView,
                                                @NonNull Interpolator interpolator) {
        Point center = getCenterForView(startView);
        return createCircularReveal(center, startView.getWidth(), targetView, interpolator);
    }

    /**
     * Create a simple circular reveal from a given start view to it's target view.
     * This reveal will start from the start view's boundaries until it fills the target view.
     *
     * @param center The center x and y coordinates of the start circle.
     * @param width The initial width of the view's coordinates.
     * @param targetView The target view which will be displayed once the reveal is done.
     * @param interpolator The interpolator to use.
     * @return The created circular reveal.
     */
    @NonNull
    public static Animator createCircularReveal(@NonNull Point center,
                                                int width,
                                                @NonNull View targetView,
                                                @NonNull Interpolator interpolator) {
        final Animator circularReveal = ViewAnimationUtils.createCircularReveal(targetView,
                center.x, center.y, width, (float) Math.hypot(center.x, center.y));
        circularReveal.setInterpolator(interpolator);
        return circularReveal;
    }

    /**
     * center를 조정하여 animation가능하다.
     *
     * @param center The center x and y coordinates of the final circle.
     * @param targetView The view which will be hidden after the animation.
     * @param interpolator The interpolator to use.
     * @return The created circular conceal.
     */
    public static Animator createCircularConceal(@NonNull Point center,
                                                 @NonNull View targetView,
                                                 @NonNull Interpolator interpolator) {
        final Animator circularReveal = ViewAnimationUtils.createCircularReveal(targetView,
                center.x, center.y, (float) Math.hypot(center.x, center.y), 0);
        circularReveal.setInterpolator(interpolator);
        return circularReveal;
    }
    /**
     * 전체화면된 view를 감추기 위해 사용한다. 기본 center로 감춰진다.
     *
     * @param startView The view which will initially displayed.
     * @param interpolator The interpolator to use.
     * @return The created circular conceal.
     */
    public static Animator createCircularConceal(@NonNull View startView, @NonNull Interpolator interpolator) {
        return createCircularConceal( getCenterForView(startView), startView, interpolator);
    }
}
