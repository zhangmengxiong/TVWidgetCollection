package com.mx.widget.animator

import android.graphics.Rect
import android.view.View

/**
 * 创建人： zhangmengxiong
 * 创建时间： 2016-10-13.
 * 联系方式: zmx_final@163.com
 */

interface IBaseAnimator {
    fun setOnFocusView(focusView: View?, floatView: View, paddingRect: Rect)

    fun setAnimation(scale: Float, duration: Long)

    fun setMoveDuration(duration: Long)
}
