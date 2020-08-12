/*
 * Copyright 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.motion.demo.fadethrough

import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.transition.TransitionManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.example.android.motion.R
import com.example.android.motion.demo.MEDIUM_COLLAPSE_DURATION
import com.example.android.motion.demo.MEDIUM_EXPAND_DURATION
import com.example.android.motion.demo.fadeThrough
import com.example.android.motion.ui.EdgeToEdge
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView

/**
 * @View.isVisible View의 Visibility를 true,false로 조정하는 변수... Visible / Gone
 * TransitionManager.beginDelayedTransition는 기본적으로 Layout의 변화와 FadeIn효과를 준다.
 * 여기에 fadeThrough()를 추가로 주어서 fadeOut효과도 같이 준다!
 */
class FadeThroughActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fade_through_activity)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        val card: MaterialCardView = findViewById(R.id.card)
        val contact: ConstraintLayout = findViewById(R.id.card_contact)
        val cheese: ConstraintLayout = findViewById(R.id.card_cheese)
        val root1: RelativeLayout = findViewById(R.id.root1)
        val root2: MaterialCardView = findViewById(R.id.root2)
        val test1: LinearLayout = findViewById(R.id.test1)
        val test2: LinearLayout = findViewById(R.id.test2)
        val toggle: MaterialButton = findViewById(R.id.toggle)
        val icon: ImageView = findViewById(R.id.contact_icon)

        // Set up the layout.
        setSupportActionBar(toolbar)
        EdgeToEdge.setUpRoot(findViewById(R.id.root))
        EdgeToEdge.setUpAppBar(findViewById(R.id.app_bar), toolbar)
        EdgeToEdge.setUpScrollingContent(findViewById(R.id.content))
        Glide.with(icon).load(R.drawable.cheese_2).transform(CircleCrop()).into(icon)

        // This is the transition we use for the fade-through effect.
        val fadeThrough = fadeThrough()

        toggle.setOnClickListener {
            // We are only toggling the visibilities of the card contents here.
            if (test1.isVisible) {
                // Delays the fade-through transition until the layout change below takes effect.
                TransitionManager.beginDelayedTransition(
                    root2,
                    fadeThrough.setDuration(MEDIUM_EXPAND_DURATION)
                )
                test1.isVisible = false
                test2.isVisible = true
            } else {
                TransitionManager.beginDelayedTransition(
                    root2,
                    fadeThrough.setDuration(MEDIUM_COLLAPSE_DURATION)
                )
                test1.isVisible = true
                test2.isVisible = false
            }
        }
    }

}
