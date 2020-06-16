package com.example.android.drawableanimations

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

class CardFlipActivity : FragmentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_flip)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.container, CardFrontFragment())
                .commit()
        }
    }




    /**
     * A fragment representing the front of the card.
     */
    class CardFrontFragment(val number : Int = 0) : Fragment() {

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View = inflater.inflate(R.layout.fragment_card_front, container, false).apply {
            findViewById<TextView>(R.id.tv_number).text = ""+number
            setOnClickListener {
                (activity as CardFlipActivity).flipCard(number, false, false)
            }
        }
    }

    /**
     * A fragment representing the back of the card.
     */

    class CardBackFragment(val number : Int = 0) : Fragment() {

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View = inflater.inflate(R.layout.fragment_card_back, container, false).apply {
            findViewById<TextView>(R.id.tv_number).text = ""+number
            setOnClickListener {
                (activity as CardFlipActivity).flipCard(number,false, true)
            }
        }
    }



    private fun flipCard( number: Int, showingBack: Boolean , isSecondFragment : Boolean ) {
        if (showingBack) {
            supportFragmentManager.popBackStack()
            return
        }

        // Create and commit a new fragment transaction that adds the fragment for
        // the back of the card, uses custom animations, and is part of the fragment
        // manager's back stack.

        supportFragmentManager.beginTransaction()

            // Replace the default fragment animations with animator resources
            // representing rotations when switching to the back of the card, as
            // well as animator resources representing rotations when flipping
            // back to the front (e.g. when the system Back button is pressed).
            .setCustomAnimations(
                R.animator.card_flip_right_in,
                R.animator.card_flip_right_out,
                R.animator.card_flip_left_in,
                R.animator.card_flip_left_out
            )

            // Replace any fragments currently in the container view with a
            // fragment representing the next page (indicated by the
            // just-incremented currentPage variable).
            .replace(R.id.container, if(isSecondFragment) CardFrontFragment(number+1) else CardBackFragment(number+1))

            // Add this transaction to the back stack, allowing users to press
            // Back to get to the front of the card.
            .addToBackStack(null)

            // Commit the transaction.
            .commit()
    }

    override fun onBackPressed() {
        flipCard(0,true,false);
    }
}
