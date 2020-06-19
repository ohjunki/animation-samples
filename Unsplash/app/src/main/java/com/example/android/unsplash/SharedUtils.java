package com.example.android.unsplash;

import android.app.Activity;
import android.app.ActivityOptions;
import android.os.Build;
import android.util.Pair;
import android.view.View;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SharedUtils {

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
}
