package com.mx.widget.animator

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.Rect
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout

/**
 * 带动画效果的浮层
 * 创建人： zhangmengxiong
 * 创建时间： 2016-10-13.
 * 联系方式: zmx_final@163.com
 */

class MoveFocusAnimator : IBaseAnimator {

    override fun setScale(scale: Float) {

    }

    private var mAnimatorSet: AnimatorSet? = null

    override fun setOnFocusView(focusView: View?, floatView: View, paddingRect: Rect) {
        if (focusView == null) {
            floatView.visibility = View.GONE
            return
        }
        val layoutParams = floatView.layoutParams as FrameLayout.LayoutParams
        val p = IntArray(2)
        focusView.getLocationOnScreen(p)

        val newWidth = focusView.width + paddingRect.left + paddingRect.right
        val newHeight = focusView.height + paddingRect.top + paddingRect.bottom
        val oldWidth = floatView.measuredWidth
        val oldHeight = floatView.measuredHeight

        val left = p[0] - paddingRect.left
        val top = p[1] - paddingRect.top

        if (left == layoutParams.leftMargin && top == layoutParams.topMargin && floatView.visibility == View.VISIBLE) {
            return
        }

        val needReset = floatView.visibility != View.VISIBLE
        floatView.visibility = View.VISIBLE
        if (mAnimatorSet != null)
            mAnimatorSet!!.cancel()
        floatView.clearAnimation()

        val transAnimatorX: ObjectAnimator
        val transAnimatorY: ObjectAnimator
        val scaleXAnimator: ObjectAnimator
        val scaleYAnimator: ObjectAnimator
        if (needReset) {
            transAnimatorX = ObjectAnimator.ofFloat(floatView, "translationX", (left + focusView.width / 2).toFloat(), left.toFloat())
            transAnimatorY = ObjectAnimator.ofFloat(floatView, "translationY", (top + focusView.height / 2).toFloat(), top.toFloat())
            scaleXAnimator = ObjectAnimator.ofInt(ScaleView(floatView), "width", 0, newWidth)
            scaleYAnimator = ObjectAnimator.ofInt(ScaleView(floatView), "height", 0, newHeight)
        } else {
            transAnimatorX = ObjectAnimator.ofFloat(floatView, "translationX", left.toFloat())
            transAnimatorY = ObjectAnimator.ofFloat(floatView, "translationY", top.toFloat())
            scaleXAnimator = ObjectAnimator.ofInt(ScaleView(floatView), "width", oldWidth, newWidth)
            scaleYAnimator = ObjectAnimator.ofInt(ScaleView(floatView), "height", oldHeight, newHeight)
        }
        mAnimatorSet = AnimatorSet()
        mAnimatorSet?.playTogether(transAnimatorX, transAnimatorY, scaleXAnimator, scaleYAnimator)
        mAnimatorSet?.interpolator = DecelerateInterpolator(1f)
        mAnimatorSet?.duration = 150
        mAnimatorSet?.start()
    }

    /**
     * 用於放大的view
     */
    private inner class ScaleView internal constructor(private val view: View) {
        private var width: Int = 0
        private var height: Int = 0

        fun getWidth(): Int {
            return view.layoutParams.width
        }

        fun setWidth(width: Int) {
            this.width = width
            view.layoutParams.width = width
            view.requestLayout()
        }

        fun getHeight(): Int {
            return view.layoutParams.height
        }

        fun setHeight(height: Int) {
            this.height = height
            view.layoutParams.height = height
            view.requestLayout()
        }
    }
}
