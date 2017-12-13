package com.mx.widget.views

import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import com.mx.widget.animator.IBaseAnimator
import com.mx.widget.animator.NoFocusAnimator


/**
 * 漂浮在界面上面的焦点效果层！
 * 创建人： zhangmengxiong
 * 创建时间： 2016-10-12.
 * 联系方式: zmx_final@163.com
 */

class FocusDashView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : Draw9Bitmap(context, attrs, defStyleAttr) {
    private var mPaddingSpace = 0
    private var mScale = 1.1f
    private var mDuration = 100L
    private var paddingRect: Rect? = null
    private var baseAnimator: IBaseAnimator? = null

    init {
        initView(context)
        setStroke(20f)
        setRadius(4f)
        setColors(Color.parseColor("#00FFFF"), Color.parseColor("#7700FFFF"), Color.TRANSPARENT)
        setColorsWeight(0.2f)
        baseAnimator = NoFocusAnimator()
        baseAnimator?.setAnimation(mScale, mDuration)
    }

    private fun initView(context: Context) {
        isFocusable = false
        isClickable = false
        isFocusableInTouchMode = false
    }

    fun setBaseAnimator(animator: IBaseAnimator) {
        baseAnimator = animator
    }

    fun setPadding(i: Int) {
        mPaddingSpace = i
    }

    /**
     * 设置获取焦点的View
     * 当前焦点效果层会漂浮到该View上面
     *
     * @param newFocus
     */
    fun setFocusView(newFocus: View) {
        if (paddingRect == null) {
            val size = getStroke().toInt() + mPaddingSpace
            paddingRect = Rect(size, size, size, size)
        }
        baseAnimator?.setAnimation(mScale, mDuration)
        baseAnimator?.setOnFocusView(newFocus, this, paddingRect!!)
    }
}
