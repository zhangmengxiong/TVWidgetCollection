package com.mx.widget.animator

import android.graphics.Rect
import android.view.View
import android.widget.FrameLayout

/**
 * 没有动画效果的焦点浮层
 * 创建人： zhangmengxiong
 * 创建时间： 2016-10-13.
 * 联系方式: zmx_final@163.com
 */

class NoFocusAnimator : IBaseAnimator {
    private var scaleSize: Float = 1.1f
    private var oldFocus: View? = null

    override fun setScale(scale: Float) {
        if (scale >= 1f) {
            scaleSize = scale
        }
    }

    override fun setOnFocusView(focusView: View?, floatView: View, paddingRect: Rect) {
        if (focusView == null) {
            floatView.visibility = View.GONE
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
            val anima = AnimationBiz.createScaleAnimation(scaleSize, 200)
            focusView.bringToFront()
            focusView.startAnimation(anima)
        }
        oldFocus?.clearAnimation()

        oldFocus = focusView
        layoutParams.leftMargin = left
        layoutParams.topMargin = top
        layoutParams.width = newWidth
        layoutParams.height = newHeight
        floatView.layoutParams = layoutParams
        floatView.visibility = View.VISIBLE
    }
}
