package com.mx.widget.views

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.widget.ListView

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

    fun setDefualtSelect(pos: Int) {
        requestFocusFromTouch()
        setSelection(pos)
    }
}
