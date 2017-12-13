package com.mx.widget.views

import android.content.Context
import android.util.AttributeSet
import android.widget.GridView

/**
 * GridView TV版本.
 *
 * @author hailongqiu 356752238@qq.com
 */
class TVGridView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : GridView(context, attrs) {

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

    fun setDefualtSelect(pos: Int) {
        requestFocusFromTouch()
        setSelection(pos)
    }
}