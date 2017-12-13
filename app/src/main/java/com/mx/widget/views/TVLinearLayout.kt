package com.mx.widget.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout

/**
 * 如果有控件放大被挡住，可以使用 TVLinearLayout,
 * 它继承于 LinearLayout.
 * 使用方式和LinerLayout是一样的
 * @author hailongqiu
 */
class TVLinearLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : LinearLayout(context, attrs, defStyleAttr) {
    private var frontChildIndex = -1

    init {
        this.isChildrenDrawingOrderEnabled = true
        frontChildIndex = -1
    }

    override fun bringChildToFront(child: View) {
        frontChildIndex = indexOfChild(child)
        if (frontChildIndex >= 0) {
            postInvalidate()
        }
    }

    override fun getChildDrawingOrder(childCount: Int, i: Int): Int {
        if (frontChildIndex >= 0) {
            if (i == childCount - 1) {
                return frontChildIndex
            }
            if (frontChildIndex == i) {
                return childCount - 1
            }
        }
        return i
    }
}
