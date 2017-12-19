package com.mx.widget.activity

import android.app.Activity
import android.os.Bundle
import android.view.View
import com.mx.widget.R
import com.mx.widget.animator.MoveFocusAnimator
import kotlinx.android.synthetic.main.activity_global_focus_view.*

class GlobalFocusViewActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_global_focus_view)
        focusView.setRadius(5f)
        focusView.setStroke(15f)
        focusView.visibility = View.GONE
        focusView.setBaseAnimator(MoveFocusAnimator())
        with(rootLay.viewTreeObserver) {
            addOnGlobalFocusChangeListener { oldFocus, newFocus ->
                focusView.setFocusView(newFocus)
            }
            addOnScrollChangedListener {

            }
        }
    }
}
