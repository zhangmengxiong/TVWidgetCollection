package com.mx.widget.views

import android.content.Context
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

    init {
        initView(context)
        mColorWidth = 20f
    }

    private var paddingRect: Rect? = null
    private var baseAnimator: IBaseAnimator? = null

    private fun initView(context: Context) {
        isFocusable = false
        isClickable = false
        isFocusableInTouchMode = false

        mColorWidth = resources.displayMetrics.density * 8
    }

    fun setStokeWidth(w: Float) {
        mColorWidth = w
        postInvalidate()
    }

    fun setStokeColor() {

    }

    /**
     * 设置阴影的边框值
     *
     * @param rect
     */
    fun setPaddingRect(rect: Rect) {
        rect.left += mPaddingSpace
        rect.right += mPaddingSpace
        rect.top += mPaddingSpace
        rect.bottom += mPaddingSpace

        paddingRect = rect
    }

    fun setBaseAnimator(animator: IBaseAnimator) {
        baseAnimator = animator
    }

    /**
     * 设置获取焦点的View
     * 当前焦点效果层会漂浮到该View上面
     *
     * @param newFocus
     */
    fun setFocusView(newFocus: View) {
        if (baseAnimator == null) baseAnimator = NoFocusAnimator()
        if (paddingRect == null) {
            val size = mColorWidth.toInt() + mPaddingSpace
            paddingRect = Rect(size, size, size, size)
        }

        baseAnimator?.setOnFocusView(newFocus, this, paddingRect!!)
    }
}
