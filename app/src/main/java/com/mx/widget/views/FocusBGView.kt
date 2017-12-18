package com.mx.widget.views

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import com.mx.widget.animator.IBaseAnimator
import com.mx.widget.animator.NoFocusAnimator

/**
 * Created by ZMX on 2017/12/18.
 */

class FocusBGView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : ImageView(context, attrs, defStyleAttr) {
    private var mPaddingSpace = 0.0f
    private var mScale = 1.0f
    private var mDuration = 100L
    private var paddingRect: Rect? = null
    private var baseAnimator: IBaseAnimator? = null

    init {
        isFocusable = false
        isClickable = false
        isFocusableInTouchMode = false

        baseAnimator = NoFocusAnimator()
        baseAnimator?.setAnimation(mScale, mDuration)

        visibility = View.GONE
    }

    fun setBaseAnimator(animator: IBaseAnimator) {
        baseAnimator = animator
    }

    fun setPadding(i: Float) {
        mPaddingSpace = i
    }

    fun setScale(scale: Float, duration: Long) {
        mScale = scale
        mDuration = duration
        baseAnimator?.setAnimation(mScale, mDuration)
    }

    /**
     * 设置获取焦点的View
     * 当前焦点效果层会漂浮到该View上面
     *
     * @param newFocus
     */
    fun setFocusView(newFocus: View) {
        if (paddingRect == null) {
            val size = mPaddingSpace.toInt()
            paddingRect = Rect(size, size, size, size)
        }
        baseAnimator?.setAnimation(mScale, mDuration)
        baseAnimator?.setMoveDuration(mDuration)
        baseAnimator?.setOnFocusView(newFocus, this, paddingRect!!)
    }
}
