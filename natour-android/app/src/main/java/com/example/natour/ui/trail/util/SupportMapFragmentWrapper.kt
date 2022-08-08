package com.example.natour.ui.trail.util

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.SupportMapFragment

/* This class resolves conflicts between the ScrollView and the Google map */

class SupportMapFragmentWrapper : SupportMapFragment() {
    private lateinit var mListener: () -> Unit

    fun setOnTouchListener(listener: () -> Unit) {
        mListener = listener
    }

    inner class TouchableWrapper(context: Context) : FrameLayout(context) {
        override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
            when(ev!!.action) {
                MotionEvent.ACTION_DOWN -> mListener.invoke()
                MotionEvent.ACTION_UP   -> mListener.invoke()
            }
            return super.dispatchTouchEvent(ev)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val layout = super.onCreateView(inflater, container, savedInstanceState)

        val frameLayout = TouchableWrapper(requireContext())
        frameLayout.setBackgroundColor(
            ContextCompat.getColor(
                activity?.applicationContext!!,
                android.R.color.transparent)
        )

        with(layout as ViewGroup) {
            addView(
                frameLayout,
                ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            )
        }

        return layout
    }
}