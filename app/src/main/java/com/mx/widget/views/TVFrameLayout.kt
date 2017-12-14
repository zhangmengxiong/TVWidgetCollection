package com.mx.widget.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout

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
class TVFrameLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : FrameLayout(context, attrs, defStyleAttr) {
    private var frontChildIndex = -1

    init {
        this.isChildrenDrawingOrderEnabled = true
        frontChildIndex = -1
        clipChildren = false // 是否限制子View超出当前ViewGroup的绘制
        clipToPadding = false // 是否限制到边框
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
