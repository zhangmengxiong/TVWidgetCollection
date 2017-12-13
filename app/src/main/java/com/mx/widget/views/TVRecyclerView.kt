package com.mx.widget.views

import android.content.Context
import android.graphics.Canvas
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.View


/**
 * Created by ZMX on 2017/12/11.
 */

class TVRecyclerView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {
    private var frontChildIndex = -1

    init {
        isChildrenDrawingOrderEnabled = true
        frontChildIndex = -1
    }

    /**
     * 重写
     */
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

    override fun requestChildFocus(child: View?, focused: View?) {
        if (focusCenterInView) {
            child?.let {
                it.bringToFront()
                scrollToCenter(it)
            }
        }
        super.requestChildFocus(child, focused)
    }

    private var focusCenterInView = false
    /**
     * 设置焦点永远在ViewGroup中间
     */
    fun setSelectedItemAtCentered(center: Boolean) {
        focusCenterInView = center
    }

    override fun onChildAttachedToWindow(child: View?) {
        child?.setOnClickListener {
            recycleCall?.onItemClick(getChildAdapterPosition(it), it)
        }
        child?.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                recycleCall?.onItemSelect(getChildAdapterPosition(v), v)
            }
        }

        super.onChildAttachedToWindow(child)
    }

    private var recycleCall: RecycleCall? = null

    /**
     * item点击、选中回调
     */
    fun setRecycleCall(call: RecycleCall) {
        this.recycleCall = call
    }

    /**
     * 最后的位置.
     */
    private fun findLastVisibleItemPosition(): Int {
        val layoutManager = layoutManager
        if (layoutManager != null) {
            if (layoutManager is LinearLayoutManager) {
                return layoutManager.findLastVisibleItemPosition()
            }
            if (layoutManager is GridLayoutManager) {
                return layoutManager.findLastVisibleItemPosition()
            }
        }
        return RecyclerView.NO_POSITION
    }

    /**
     * 滑动到底部.
     */
    private fun findLastCompletelyVisibleItemPosition(): Int {
        val layoutManager = layoutManager
        if (layoutManager != null) {
            if (layoutManager is LinearLayoutManager) {
                return layoutManager.findLastCompletelyVisibleItemPosition()
            }
            if (layoutManager is GridLayoutManager) {
                return layoutManager.findLastCompletelyVisibleItemPosition()
            }
        }
        return RecyclerView.NO_POSITION
    }

    /**
     * 找到第一个显示的position
     */
    fun findFirstVisibleItemPosition(): Int {
        val lm = layoutManager
        if (lm != null) {
            if (lm is LinearLayoutManager) {
                return lm.findFirstVisibleItemPosition()
            }
            if (lm is GridLayoutManager) {
                return lm.findFirstVisibleItemPosition()
            }
        }
        return RecyclerView.NO_POSITION
    }

    /**
     * 设置选中位置，自动获取焦点
     */
    fun setDefaultSelect(int: Int) {
        val first = findFirstVisibleItemPosition()
        val last = findLastCompletelyVisibleItemPosition()
        if (int !in first..last) {
            scrollToPosition(int)
        }
        val vh = findViewHolderForAdapterPosition(int)
        if (vh?.itemView != null) {
            requestFocusFromTouch()
            vh.itemView?.let {
                it.requestFocus()
                it.requestFocusFromTouch()
                scrollToCenter(it)
            }
        } else {
            post {
                if (!isShown) return@post

                val vh1 = findViewHolderForAdapterPosition(int)
                requestFocusFromTouch()
                vh1.itemView?.let {
                    it.requestFocus()
                    it.requestFocusFromTouch()
                    scrollToCenter(it)
                }
            }
        }
    }

    override fun dispatchKeyEvent(event: KeyEvent?): Boolean {
        val event = event ?: return super.dispatchKeyEvent(event)
        onKeyCall?.let {
            if (event.action == KeyEvent.ACTION_DOWN
                    && event.keyCode in arrayOf(KeyEvent.KEYCODE_DPAD_RIGHT,
                    KeyEvent.KEYCODE_DPAD_DOWN,
                    KeyEvent.KEYCODE_DPAD_LEFT,
                    KeyEvent.KEYCODE_DPAD_UP)) {
                if (it(event.keyCode)) {
                    return true
                }
            }
        }
        return super.dispatchKeyEvent(event)
    }

    /**
     * 滑动过快时可能会报错
     */
    override fun draw(c: Canvas?) {
        try {
            super.draw(c)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 将child滚动到中间位置
     */
    private fun scrollToCenter(child: View) {
        stopScroll()
        if (isVertical()) {
            val dy = (child.top + child.height / 2) - (scrollY + height / 2)
            println("dy = $dy")
            smoothScrollBy(0, dy)
        } else {
            val dx = (child.left + child.width / 2) - (scrollX + width / 2)
            println("dx = $dx")
            smoothScrollBy(dx, 0)
        }
    }

    /**
     * 是否为竖向布局
     */
    private fun isVertical(): Boolean {
        val lm = layoutManager
        if (lm != null) {
            if (lm is LinearLayoutManager) {
                return LinearLayoutManager.VERTICAL == lm.orientation
            }
            if (lm is GridLayoutManager) {
                return GridLayoutManager.VERTICAL == lm.orientation
            }

            return lm.canScrollVertically()
        }
        return false
    }

    private var onKeyCall: ((keyCode: Int) -> Boolean)? = null
    fun setOnKey(action: (keyCode: Int) -> Boolean) {
        onKeyCall = action
    }

    open class RecycleCall {
        /**
         * 点击响应
         */
        open fun onItemClick(position: Int, view: View) = Unit

        open fun onItemSelect(position: Int, view: View) = Unit
    }
}