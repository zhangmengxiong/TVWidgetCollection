package com.mx.widget.shadeimg

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
class FocusFloatView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : ShadeBitmap(context, attrs, defStyleAttr) {
    private var mPaddingSpace = 0.0f
    private var mScale = 1.0f
    private var mDuration = 100L
    private var paddingRect: Rect? = null
    private var baseAnimator: IBaseAnimator? = null

    init {
        isFocusable = false
        isClickable = false
        isFocusableInTouchMode = false

        setStroke(20f)
        setRadius(0f)
        setColors(Color.parseColor("#3F51B5"), Color.parseColor("#773F51B5"), Color.parseColor("#003F51B5"))
        setColorsWeight(0.3f)
        baseAnimator = NoFocusAnimator()
        baseAnimator?.setAnimation(mScale, mDuration)

        visibility = GONE
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
            val size = (getStroke() + mPaddingSpace).toInt()
            paddingRect = Rect(size, size, size, size)
        }
        baseAnimator?.setAnimation(mScale, mDuration)
        baseAnimator?.setMoveDuration(mDuration)
        baseAnimator?.setOnFocusView(newFocus, this, paddingRect!!)
    }

    fun hide() {
        baseAnimator?.cancelAnimator()
        visibility = View.GONE
    }
}
