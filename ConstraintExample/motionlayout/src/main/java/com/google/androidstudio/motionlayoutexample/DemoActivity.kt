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

package com.google.androidstudio.motionlayoutexample

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.transition.TransitionManager
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.androidstudio.motionlayoutexample.utils.DLog
import com.google.androidstudio.motionlayoutexample.utils.SharedUtils
import kotlinx.android.synthetic.main.main_activity.view.*


@RequiresApi(Build.VERSION_CODES.LOLLIPOP) // for View#clipToOutline
class DemoActivity : AppCompatActivity() {

    private lateinit var container: View
    class TempFloating(context: Context?, attrs: AttributeSet?) : FloatingActionButton(context, attrs) {
        public fun showHide(){
            DLog.ee()
            if( this.visibility == View.VISIBLE ) hide()
            else    show()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val layout = intent.getIntExtra("layout_file_id", R.layout.motion_01_basic)
        setContentView(layout)
        container = findViewById(R.id.motionLayout)
        if (layout == R.layout.motion_11_coordinatorlayout) {
            val icon = findViewById<ImageView>(R.id.icon)
            icon?.clipToOutline = true
        }

        findViewById<FloatingActionButton>(R.id.fab)?.also{ fab ->
            val lp = fab.getLayoutParams() as? CoordinatorLayout.LayoutParams
            var behavior = lp?.behavior as FloatingActionButton.Behavior?
            if (behavior != null) {
                behavior.isAutoHideEnabled = false
            } else {
                behavior = FloatingActionButton.Behavior()
                behavior.isAutoHideEnabled = false
                if( lp != null )
                    lp.behavior = behavior
            }
        }
        val debugMode = if (intent.getBooleanExtra("showPaths", false)) {
            MotionLayout.DEBUG_SHOW_PATH
        } else {
            MotionLayout.DEBUG_SHOW_NONE
        }
        (container as? MotionLayout)?.setDebugMode(debugMode)

        Handler().postDelayed( Runnable {
            SharedUtils.centerViewCircleReveal( container );
        }, 2000);
    }

    /***
     * 버튼클릭 이벤트로 start/end로 보내는 경우에 아래 메소드가 호출된다.
     */
    fun changeState(v: View?) {
        val motionLayout = container as? MotionLayout ?: return
        if (motionLayout.progress > 0.5f) {
            motionLayout.transitionToStart()
        } else {
            motionLayout.transitionToEnd()
        }
    }
}