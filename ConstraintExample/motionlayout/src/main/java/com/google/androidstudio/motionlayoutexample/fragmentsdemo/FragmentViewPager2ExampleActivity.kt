package com.google.androidstudio.motionlayoutexample.fragmentsdemo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.androidstudio.motionlayoutexample.R


class FragmentViewPager2ExampleActivity : AppCompatActivity() {
    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private var mPager1: ViewPager? = null
    private var mPager: ViewPager2? = null

    /*** TODO Diff here*/
    private var pagerAdapter1: PagerAdapter? = null
    private var pagerAdapter: FragmentStateAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragment_view_pager2_example)

        // Instantiate a ViewPager and a PagerAdapter.
        mPager1 = findViewById<View>(R.id.pager1) as ViewPager
        mPager = findViewById<View>(R.id.pager) as ViewPager2

        pagerAdapter1 = ScreenSlidePagerAdapter1(supportFragmentManager)
        pagerAdapter = ScreenSlidePagerAdapter(this)
        mPager1!!.adapter = pagerAdapter1
        mPager!!.adapter = pagerAdapter
        /*** TODO Diff here*/
        mPager1!!.setPageTransformer( true, DepthPageTransformer())
        mPager!!.setPageTransformer( DepthPageTransformer())
    }

    override fun onBackPressed() {
        if (mPager!!.currentItem == 0 && mPager1!!.currentItem == 0 ) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed()
        } else {
            // Otherwise, select the previous step.
            //TODO DiffHere
            if( mPager!!.currentItem != 0)
                mPager!!.currentItem = mPager!!.currentItem - 1
            if( mPager1!!.currentItem != 0)
                mPager1!!.currentItem = mPager1!!.currentItem - 1
        }
    }

    /*** TODO Diff here*/
    private class ScreenSlidePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int {
            return 5
        }

        override fun createFragment(position: Int): Fragment {
            return ScreenSlidePageFragment()
        }
    }


    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter1(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
        override fun getItem(position: Int): Fragment {
            return ScreenSlidePageFragment()
        }

        override fun getCount(): Int {
            return 5
        }
    }
}


class ScreenSlidePageFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(
            R.layout.fragment_screen_slide_page, container, false
        ) as ViewGroup
    }
}