/*
 * Copyright (C) 2014 The Android Open Source Project
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

package com.example.android.customtransition;

import com.example.android.common.logger.Log;

import android.content.Context;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.Scene;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.FlingAnimation;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.fragment.app.Fragment;

public class CustomTransitionFragment extends Fragment implements View.OnClickListener {

    private static final String STATE_CURRENT_SCENE = "current_scene";

    /** Tag for the logger */
    private static final String TAG = "CustomTransitionFragment";

    /** These are the Scenes we use. */
    private Scene[] mScenes;

    /** The current index for mScenes. */
    private int mCurrentScene;

    /** This is the custom Transition we use in this sample. */
    private Transition mTransition;

    public CustomTransitionFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_custom_transition, container, false);
        ViewGroup sceneRoot = (FrameLayout) view.findViewById(R.id.scene_root);
        view.findViewById(R.id.show_next_scene).setOnClickListener(this);
        if (null != savedInstanceState) {
            mCurrentScene = savedInstanceState.getInt(STATE_CURRENT_SCENE);
        }

        View v = view.findViewById(R.id.view_1);
        SpringAnimation springAnimation = new SpringAnimation(v, DynamicAnimation.TRANSLATION_Y,0);
        springAnimation.start();
        FlingAnimation fling = new FlingAnimation(view, DynamicAnimation.SCROLL_X);

        // We set up the Scenes here.
        mScenes = new Scene[] {
                new Scene( sceneRoot, (ViewGroup) sceneRoot.findViewById(R.id.container) ),
                Scene.getSceneForLayout(sceneRoot, R.layout.scene2, getContext()),
                Scene.getSceneForLayout(sceneRoot, R.layout.scene3, getContext())
        };

        // This is the custom Transition
        /**
         * 아래 Cutom Transition을 사용하면 기본 Transition은 사용이 불가능해진다!
         */
//        mTransition = new ChangeColor();
        // Show the initial Scene.
        TransitionManager.go(mScenes[mCurrentScene % mScenes.length]);

        TransitionSet set = new TransitionSet();
        set.addTransition(new ChangeColor());
        set.addTransition(new AutoTransition());
        set.addTransition(new RotateTransition());
        set.setDuration(5000);

        set.excludeTarget( R.id.container, true);
        set.addTarget( R.id.view_1 );
        set.addTarget( R.id.view_2 );
        set.addTarget( R.id.view_3 );
//        set.removeTarget( R.id.view_3);
        mTransition = set;
        return view;
    }

    /**
     * 기존 코드는 onViewCreated에서 mScenes를 생성했다
     * 이떄는 애니메이션이 제대로 동작하지 않아서 onCreateView에서 view가 생성되기 전에
     * Scene를 생성했더니 문제없이 동작한다.
     * 애니메이션은 색깔은 자동으로 되지 않지만,
     * Scale, 위치 등의 property는 자동으로 애니메이션이 되며
     * LayoutParam에 상관없고 id값만 동일하게 맞춰주면 된다!!
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_CURRENT_SCENE, mCurrentScene);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.show_next_scene: {
                mCurrentScene = (mCurrentScene + 1) % mScenes.length;
                Log.i(TAG, "Transitioning to scene #" + mCurrentScene);
                // Pass the custom Transition as second argument for TransitionManager.go
                TransitionManager.go(mScenes[mCurrentScene],mTransition);
                break;
            }
        }
    }

}
