package com.example.android.drawableanimations.demo.animated

import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.android.drawableanimations.R
import com.example.android.drawableanimations.databinding.FragmentJunkiAnimatedDrawableBinding
import com.example.android.drawableanimations.viewBindings

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [JunkiAnimatedDrawableFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class JunkiAnimatedDrawableFragment : Fragment(R.layout.fragment_junki_animated_drawable) {
    // TODO: Rename and change types of parameters
    private val binding by viewBindings( FragmentJunkiAnimatedDrawableBinding::bind )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ( binding.imgAnimatedDrawable.background as AnimationDrawable ).start()
        binding.imgVectorAnimatedDrawable.setOnClickListener {
            ( binding.imgVectorAnimatedDrawable.background as AnimatedVectorDrawable ).start()
        }
    }
}
