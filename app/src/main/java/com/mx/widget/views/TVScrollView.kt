package com.mx.widget.views

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.widget.ScrollView

/**
 * 当你使用滚动窗口焦点错乱的时候，就可以使用这个控件.
 * 使用方法和滚动窗口是一样的，具体查看DEMO吧.
 * 如果想改变滚动的系数，R.dimen.fading_edge
 * @author hailongqiu
 */
class TVScrollView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : ScrollView(context, attrs, defStyleAttr) {
    private var centerInView: Boolean = false
    private var mFadingEdge: Int = 0

    init {
        mFadingEdge = 0
        centerInView = true
//        clipChildren = false // 是否限制子View超出当前ViewGroup的绘制
//        clipToPadding = false // 是否限制到边框
    }

    fun setFadingEdge(fadingEdge: Int) {
        this.mFadingEdge = fadingEdge
    }

    fun setFocusCenterInViewGroup(c: Boolean) {
        centerInView = c
    }

    override fun computeScrollDeltaToGetChildRectOnScreen(rect: Rect): Int {
        if (childCount == 0)
            return 0
        val height = height
        var screenTop = scrollY
        var screenBottom = screenTop + height
        var fadingEdge = if (mFadingEdge > 0) mFadingEdge else 0
        if (centerInView) {
            val focusView = findFocus()
            fadingEdge = height / 2 - (focusView?.height ?: 0) / 2
        }

        if (rect.top > 0) {
            screenTop += fadingEdge
        }
        if (rect.bottom < getChildAt(0).height) {
            screenBottom -= fadingEdge
        }
        //
        var scrollYDelta = 0
        if (rect.bottom > screenBottom && rect.top > screenTop) {
            if (rect.height() > height) {
                scrollYDelta += rect.top - screenTop
            } else {
                scrollYDelta += rect.bottom - screenBottom
            }
            val bottom = getChildAt(0).bottom
            val distanceToBottom = bottom - screenBottom
            scrollYDelta = Math.min(scrollYDelta, distanceToBottom)
        } else if (rect.top < screenTop && rect.bottom < screenBottom) {
            if (rect.height() > height) {
                scrollYDelta -= screenBottom - rect.bottom
            } else {
                scrollYDelta -= screenTop - rect.top
            }
            scrollYDelta = Math.max(scrollYDelta, -scrollY)
        }
        return scrollYDelta
    }

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        TVScrollCall?.onScrollChanged(l, t, oldl, oldt)
    }

    private var TVScrollCall: TVScrollCall? = null

    /**
     * 设置监听
     */
    fun setOnScrollListener(callTV: TVScrollCall?) {
        TVScrollCall = callTV
    }
}
