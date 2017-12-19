package com.mx.widget.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.mx.widget.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rootLay.viewTreeObserver.addOnGlobalFocusChangeListener { oldFocus, newFocus ->
            newFocus?.let { focusView?.setFocusView(it) }
        }
        gridViewLayoutTxv.setOnClickListener { startActivity(Intent(this, GridViewFocusActivity::class.java)) }
        recycleViewFocusTxv.setOnClickListener { startActivity(Intent(this, RecycleFocusActivity::class.java)) }
        listFocusTxv.setOnClickListener { startActivity(Intent(this, ListFocusActivity::class.java)) }
        globalFocusTxv.setOnClickListener { startActivity(Intent(this, GlobalFocusViewActivity::class.java)) }
        tvFrameLayoutTxv.setOnClickListener { startActivity(Intent(this, TVFrameLayoutActivity::class.java)) }
        recycleView2FocusTxv.setOnClickListener { startActivity(Intent(this, Recycle2FocusActivity::class.java)) }

        shadeLineBitmap.setRadius(0f)
        shadeLineBitmap.setStroke(100f)
        focusView.setRadius(0f)

        tvHorizontalScrollView.setFocusCenterInViewGroup(true)
        globalFocusTxv.requestFocus()
    }
}
