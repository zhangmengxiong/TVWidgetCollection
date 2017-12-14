package com.mx.widget.views

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView


/**
 * Created by ZMX on 2017/12/14.
 */

class TVTextView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : TextView(context, attrs, defStyleAttr) {

    init {
//        isSelectedOrFocused = false
//
//        ellipsize = TextUtils.TruncateAt.MARQUEE
//        setSingleLine(true)
//        isSelected = true
//        isFocusable = false
//        isFocusableInTouchMode = true
    }

    fun startMarquee() {
        isSelected = true
        postInvalidate()
    }

    fun stopMarquee() {
        isSelected = false
        postInvalidate()
    }
}
