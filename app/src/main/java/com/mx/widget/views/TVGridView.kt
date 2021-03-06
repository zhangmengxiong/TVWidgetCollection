package com.mx.widget.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.AdapterView
import android.widget.GridView
import java.lang.ref.SoftReference

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
class TVGridView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : GridView(context, attrs) {
    private var frontChildView: SoftReference<View>? = null
    private var centerInView: Boolean = false
    private var itemSelectListener: OnItemSelectedListener? = null


    init {
        this.isChildrenDrawingOrderEnabled = true
//        clipChildren = false // 是否限制子View超出当前ViewGroup的绘制
//        clipToPadding = false // 是否限制到边框
        onItemSelectedListener = null
    }

    override fun bringChildToFront(child: View) {
        frontChildView = SoftReference(child)
        postInvalidate()
    }

    override fun getChildDrawingOrder(childCount: Int, i: Int): Int {
        val front = frontChildView?.get() ?: return i
        val frontChildIndex = indexOfChild(front)
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

    override fun setOnItemSelectedListener(listener: OnItemSelectedListener?) {
        itemSelectListener = listener
        super.setOnItemSelectedListener(object : OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                itemSelectListener?.onNothingSelected(parent)
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                itemSelectListener?.onItemSelected(parent, view, position, id)

                if (centerInView) {
                    /**
                     * 这一句是滚动到中间~
                     *
                     * 滚动到position时距离顶部的距离
                     * （ListView的高度减去Item的高度）/2
                     */
                    smoothScrollToPositionFromTop(position,
                            (height - (view?.height ?: 0)) / 2)
                }
            }
        })
    }

    fun setFocusCenterInViewGroup(c: Boolean) {
        centerInView = c
    }

    /**
     * 设置默认选中项
     */
    fun setDefaultSelect(pos: Int) {
        requestFocusFromTouch()
        setSelection(pos)
    }

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        tvScrollCall?.onScrollChanged(l, t, oldl, oldt)
    }

    private var tvScrollCall: TVScrollCall? = null

    /**
     * 设置监听
     */
    fun setOnTVScrollListener(callTV: TVScrollCall?) {
        tvScrollCall = callTV
    }
}