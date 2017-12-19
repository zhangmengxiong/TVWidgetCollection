package com.mx.widget.animator

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.Rect
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import java.lang.ref.SoftReference
import kotlin.math.max

/**
 * 带动画效果的浮层
 * 创建人： zhangmengxiong
 * 创建时间： 2016-10-13.
 * 联系方式: zmx_final@163.com
 */

class MoveFocusAnimator : IBaseAnimator {
    private var scaleSize: Float = 1.3f
    private var scaleDuration = 150L
    private var moveDuration = 150L
    private var oldFocus: SoftReference<View>? = null

    override fun setAnimation(scale: Float, duration: Long) {
        scaleSize = max(1f, scale)
        scaleDuration = max(100L, duration)
    }

    override fun setMoveDuration(duration: Long) {
        moveDuration = duration
    }

    override fun cancelAnimator() {
        oldFocus?.get()?.clearAnimation()
        oldFocus = null
    }

    private var mAnimatorSet: AnimatorSet? = null

    override fun setOnFocusView(focusView: View?, floatView: View, paddingRect: Rect) {
        synchronized(this) {
            oldFocus?.get()?.clearAnimation()
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
            val oldWidth = floatView.measuredWidth
            val oldHeight = floatView.measuredHeight

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

            if (left == layoutParams.leftMargin
                    && top == layoutParams.topMargin
                    && oldWidth == newWidth
                    && oldHeight == newHeight
                    && oldFocus?.get() == focusView
                    && floatView.visibility == View.VISIBLE) {
                /**
                 * 重复位移校验
                 * 当新的位移和旧的位移位置、大小一致，且焦点View一样时，不作处理！
                 */

                layoutParams.leftMargin = left
                layoutParams.topMargin = top
                layoutParams.width = newWidth
                layoutParams.height = newHeight
                floatView.layoutParams = layoutParams
                floatView.postInvalidate()
                return
            }

            val needReset = floatView.visibility != View.VISIBLE
            floatView.visibility = View.VISIBLE
            mAnimatorSet?.cancel()
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

            if (scaleSize > 1f) {
                focusView.clearAnimation()
                focusView.startAnimation(AnimationBiz.createIncreaseScaleAnimation(scaleSize, scaleDuration))

                val oldFocus = oldFocus?.get()
                if (oldFocus != focusView) {
                    oldFocus?.startAnimation(AnimationBiz.createDecreaseScaleAnimation(scaleSize, scaleDuration))
                }
                this.oldFocus = SoftReference(focusView)
            }

            mAnimatorSet = AnimatorSet()
            mAnimatorSet?.playTogether(transAnimatorX, transAnimatorY, scaleXAnimator, scaleYAnimator)
            mAnimatorSet?.interpolator = DecelerateInterpolator(1f)
            mAnimatorSet?.duration = moveDuration
            mAnimatorSet?.start()
        }
    }

    /**
     * 用於放大的view
     */
    private class ScaleView(private val view: View) {
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
