package com.google.androidstudio.motionlayoutexample.utils;

import android.animation.Animator;
import android.app.Activity;
import android.app.ActivityOptions;
import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SharedUtils {

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private static ActivityOptions getActivityOptions(Activity activity , Pair<View, String>... elements ) {
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

    public static void centerViewCircleReveal(View view) {
        Animator circularReveal = ViewAnimationUtils.createCircularReveal(
                view,
                (int) (view.getWidth()/2),
                (int) (view.getHeight()/2),
                0,
                (float) Math.hypot(view.getWidth(), view.getHeight()));
        circularReveal.setInterpolator(new AccelerateDecelerateInterpolator());
        circularReveal.setDuration(1000);
        circularReveal.start();
    }
}

