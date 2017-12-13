package com.mx.widget.views

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.FocusFinder
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
                println("top = ${child.top}")
                println("height = ${child.height}")
                println("scrollY = $scrollY")
                println("height = $height")

                it.bringToFront()
                if (isVertical()) {
                    val dy = (child.top + child.height / 2) - (scrollY + height / 2 - paddingBottom - paddingTop)
                    smoothScrollBy(0, dy)
                } else {
                    val dx = (child.left + child.width / 2) - (scrollX + width / 2 - paddingLeft - paddingRight)
                    smoothScrollBy(dx, 0)
                }
            }
        }
        super.requestChildFocus(child, focused)
    }

    private var focusCenterInView = false
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

    /**
     * RecycleView内焦点转出时回调处理！
     */
    override fun dispatchKeyEvent(event: KeyEvent?): Boolean {
        event?.let {
            val keyCode = event.keyCode
            if (event.action == KeyEvent.ACTION_DOWN) {
                when (keyCode) {
                    KeyEvent.KEYCODE_DPAD_LEFT -> {
                        println("KEYCODE_DPAD_LEFT")
                        if (FocusFinder.getInstance().findNextFocus(this, focusedChild, View.FOCUS_LEFT) == null
                                && recycleCall?.onKeyLeft() == true) {
                            return true
                        }
                    }
                    KeyEvent.KEYCODE_DPAD_RIGHT -> {
                        println("KEYCODE_DPAD_RIGHT")
                        if (FocusFinder.getInstance().findNextFocus(this, focusedChild, View.FOCUS_RIGHT) == null
                                && recycleCall?.onKeyRight() == true) {
                            return true
                        }
                    }
                    KeyEvent.KEYCODE_DPAD_UP -> {
                        println("KEYCODE_DPAD_UP")
                        if (FocusFinder.getInstance().findNextFocus(this, focusedChild, View.FOCUS_UP) == null
                                && recycleCall?.onKeyUp() == true) {
                            return true
                        }
                    }
                    KeyEvent.KEYCODE_DPAD_DOWN -> {
                        println("KEYCODE_DPAD_DOWN")
                        if (FocusFinder.getInstance().findNextFocus(this, focusedChild, View.FOCUS_DOWN) == null
                                && recycleCall?.onKeyDown() == true) {
                            return true
                        }
                    }
                    else -> {

                    }
                }
//                nextFocus?.let {
//                    println("下一个焦点找到！")
//                }
            }
        }
        return super.dispatchKeyEvent(event)
    }

    private var recycleCall: RecycleCall? = null

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

    fun setDefaultSelect(int: Int) {
        val first = findFirstVisibleItemPosition()
        val last = findLastCompletelyVisibleItemPosition()
        if (int !in first..last) {
            scrollToPosition(int)
        }
        val vh = findViewHolderForAdapterPosition(int)
        if (vh?.itemView != null) {
            requestFocusFromTouch()
            vh.itemView?.requestFocus()
        } else {
            post {
                val vh1 = findViewHolderForAdapterPosition(int)
                requestFocusFromTouch()
                vh1?.itemView?.requestFocus()
            }
        }
    }

    fun isVertical(): Boolean {
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
}

open class RecycleCall {
    /**
     * 当前ViewGroup内焦点往左跳转，焦点处理！
     * 返回true时不再分发事件
     */
    open fun onKeyLeft(): Boolean = false

    open fun onKeyRight(): Boolean = false
    open fun onKeyUp(): Boolean = false
    open fun onKeyDown(): Boolean = false

    /**
     * 点击响应
     */
    open fun onItemClick(position: Int, view: View) = Unit

    open fun onItemSelect(position: Int, view: View) = Unit
}