package com.mx.widget.views

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.widget.TextView


/**
 * Created by ZMX on 2017/12/14.
 */

class TVTextView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : TextView(context, attrs, defStyleAttr) {
    init {
    }

    fun startMarquee() {
        setSingleLine()
        isFocusable = false
        isFocusableInTouchMode = false
        ellipsize = TextUtils.TruncateAt.MARQUEE
        marqueeRepeatLimit = -1

        isSelected = true
        postInvalidate()
    }

    fun stopMarquee() {
        isSelected = false
        postInvalidate()
    }
}
