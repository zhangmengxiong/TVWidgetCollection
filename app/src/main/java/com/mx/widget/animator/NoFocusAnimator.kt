package com.mx.widget.animator

import android.graphics.Rect
import android.view.View
import android.widget.FrameLayout
import java.lang.ref.SoftReference

/**
 * 没有动画效果的焦点浮层
 * 创建人： zhangmengxiong
 * 创建时间： 2016-10-13.
 * 联系方式: zmx_final@163.com
 */

class NoFocusAnimator : IBaseAnimator {
    private var scaleSize: Float = 1.3f
    private var scaleDuration = 100L
    private var oldFocus: SoftReference<View>? = null

    override fun setAnimation(scale: Float, duration: Long) {
        if (scale >= 1f) {
            scaleSize = scale
        }
        if (duration > 0) {
            scaleDuration = duration
        }
    }

    override fun setOnFocusView(focusView: View?, floatView: View, paddingRect: Rect) {
        if (focusView == null) {
            floatView.visibility = View.GONE
            oldFocus = null
            return
        }
        val layoutParams = floatView.layoutParams as FrameLayout.LayoutParams

        val p = IntArray(2)
        focusView.getLocationOnScreen(p)

        val newWidth = focusView.width + paddingRect.left + paddingRect.right + if (scaleSize > 1) {
            (focusView.width * (scaleSize - 1)).toInt()
        } else {
            0
        }
        val newHeight = focusView.height + paddingRect.top + paddingRect.bottom + if (scaleSize > 1) {
            (focusView.height * (scaleSize - 1)).toInt()
        } else {
            0
        }

        val left = p[0] - paddingRect.left - if (scaleSize > 1) {
            (focusView.width * (scaleSize - 1) / 2f).toInt()
        } else {
            0
        }
        val top = p[1] - paddingRect.top - if (scaleSize > 1) {
            (focusView.height * (scaleSize - 1) / 2f).toInt()
        } else {
            0
        }

        if (left == layoutParams.leftMargin && top == layoutParams.topMargin && floatView.visibility == View.VISIBLE) {
            return
        }

        if (scaleSize > 1) {
            val anima = AnimationBiz.createIncreaseScaleAnimation(scaleSize, scaleDuration)
            focusView.clearAnimation()
            focusView.bringToFront()
            focusView.startAnimation(anima)
        }
        val oldFocus = oldFocus?.get()
        if (oldFocus != focusView) {
            oldFocus?.let {
                it.clearAnimation()
                val anima = AnimationBiz.createDecreaseScaleAnimation(scaleSize, scaleDuration)
                it.startAnimation(anima)
            }
            this.oldFocus = SoftReference(focusView)
        }

        layoutParams.leftMargin = left
        layoutParams.topMargin = top
        layoutParams.width = newWidth
        layoutParams.height = newHeight
        floatView.layoutParams = layoutParams
        floatView.visibility = View.VISIBLE
    }
}
