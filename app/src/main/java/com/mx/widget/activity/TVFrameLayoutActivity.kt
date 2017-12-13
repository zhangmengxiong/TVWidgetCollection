package com.mx.widget.activity

import android.app.Activity
import android.os.Bundle
import com.mx.widget.R
import kotlinx.android.synthetic.main.tv_frame_layout.*

/**
 * Created by ZMX on 2017/12/13.
 */
class TVFrameLayoutActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tv_frame_layout)

        rootLay.viewTreeObserver.addOnGlobalFocusChangeListener { oldFocus, newFocus ->
            newFocus.bringToFront()
            focusView.setFocusView(newFocus)
        }
        rootLay.viewTreeObserver?.addOnScrollChangedListener {
            rootLay.findFocus()?.let { focusView.setFocusView(it) }
        }
//        focusView.setBaseAnimator(Move)
    }
}