package com.mx.widget.views

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.widget.ListView

/**
 * 功能：
 * 1：子View可以超出界限
 * 2：子View调用bringToFront()时，当前ViewGroup会将这个View排列到最后一个来Drawing，防止子View被容器内其他View挡住！
 *
 * 实现说明：
 * isChildrenDrawingOrderEnabled = true // 设置在这个ViewGroup内需要重新排序画子View的顺序
 * bringChildToFront() 重载：将front的View的Index找到并存储
 * getChildDrawingOrder() 重载：获取子View的绘制顺序，将需要显示在最前的子View放在最后一个Drawing
 *
 * @author zmx_final@163.com
 */
class TVListView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : ListView(context, attrs, defStyleAttr) {
    /**
     * 崩溃了.
     */
    override fun dispatchDraw(canvas: Canvas) {
        try {
            super.dispatchDraw(canvas)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    init {
        this.isChildrenDrawingOrderEnabled = true
        clipChildren = false
        clipToPadding = false
    }

    override fun isInTouchMode(): Boolean {
        return !(hasFocus() && !super.isInTouchMode())
    }

    override fun getChildDrawingOrder(childCount: Int, i: Int): Int {
        if (this.selectedItemPosition != -1) {
            if (i + this.firstVisiblePosition == this.selectedItemPosition) {// 这是原本要在最后一个刷新的item
                return childCount - 1
            }
            if (i == childCount - 1) {// 这是最后一个需要刷新的item
                return this.selectedItemPosition - this.firstVisiblePosition
            }
        }
        return i
    }

    fun setDefaultSelect(pos: Int) {
        requestFocusFromTouch()
        setSelection(pos)
    }

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        scrollCall?.onScrollChanged(l, t, oldl, oldt)
    }

    private var scrollCall: ScrollCall? = null

    /**
     * 设置监听
     */
    fun setOnScrollListener(call: ScrollCall?) {
        scrollCall = call
    }
}
