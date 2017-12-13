package com.mx.widget.views

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.widget.HorizontalScrollView

/**
 * 当你使用滚动窗口焦点错乱的时候，就可以使用这个控件.
 * 使用方法和滚动窗口是一样的，具体查看DEMO吧.
 * 如果想改变滚动的系数，R.dimen.fading_edge
 * @author hailongqiu
 */
class TVHorizontalScrollView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0)
    : HorizontalScrollView(context, attrs, defStyle) {
    private var centerInView: Boolean = false
    private var mFadingEdge: Int = 0

    init {
        mFadingEdge = 10
        centerInView = false
        clipChildren = false
        clipToPadding = false
    }

    fun setFadingEdge(fadingEdge: Int) {
        this.mFadingEdge = fadingEdge
    }

    fun setFocusCenterInViewGroup(c: Boolean) {
        centerInView = c
    }

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
    }

    override fun computeScrollDeltaToGetChildRectOnScreen(rect: Rect): Int {
        if (childCount == 0)
            return 0

        val width = width
        var screenLeft = scrollX
        var screenRight = screenLeft + width

        var fadingEdge = if (mFadingEdge > 0) mFadingEdge else 0
        if (centerInView) {
            val focusView = findFocus()
            fadingEdge = width / 2 - (focusView?.width ?: 0) / 2
        }

        // leave room for left fading edge as long as rect isn't at very left
        if (rect.left > 0) {
            screenLeft += fadingEdge
        }

        // leave room for right fading edge as long as rect isn't at very right
        if (rect.right < getChildAt(0).width) {
            screenRight -= fadingEdge
        }

        var scrollXDelta = 0

        if (rect.right > screenRight && rect.left > screenLeft) {
            // need to move right to get it in view: move right just enough so
            // that the entire rectangle is in view (or at least the first
            // screen size chunk).

            if (rect.width() > width) {
                // just enough to get screen size chunk on
                scrollXDelta += rect.left - screenLeft
            } else {
                // get entire rect at right of screen
                scrollXDelta += rect.right - screenRight
            }

            // make sure we aren't scrolling beyond the end of our content
            val right = getChildAt(0).right
            val distanceToRight = right - screenRight
            scrollXDelta = Math.min(scrollXDelta, distanceToRight)

        } else if (rect.left < screenLeft && rect.right < screenRight) {
            // need to move right to get it in view: move right just enough so
            // that
            // entire rectangle is in view (or at least the first screen
            // size chunk of it).

            if (rect.width() > width) {
                // screen size chunk
                scrollXDelta -= screenRight - rect.right
            } else {
                // entire rect at left
                scrollXDelta -= screenLeft - rect.left
            }

            // make sure we aren't scrolling any further than the left our
            // content
            scrollXDelta = Math.max(scrollXDelta, -scrollX)
        }
        return scrollXDelta
    }

}
