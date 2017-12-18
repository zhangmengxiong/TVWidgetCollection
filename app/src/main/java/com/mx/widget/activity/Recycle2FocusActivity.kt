package com.mx.widget.activity

import android.app.Activity
import android.graphics.Rect
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.mx.widget.R
import com.mx.widget.adapts.MyRecycle2Adapt
import com.mx.widget.animator.MoveFocusAnimator
import com.mx.widget.views.TVRecyclerView
import com.mx.widget.views.TVTextView
import com.mx.widget.views.setOnKey
import kotlinx.android.synthetic.main.recycle2_focus_activity.*

/**
 * Created by ZMX on 2017/12/11.
 */
class Recycle2FocusActivity : Activity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.recycle2_focus_activity)

        val list = ArrayList<String>()
        (0..100).forEach {
            list.add(it.toString())
        }

        val adapter = MyRecycle2Adapt(list)
        val gridlayoutManager = LinearLayoutManager(this) // 解决快速长按焦点丢失问题.
        gridlayoutManager.orientation = LinearLayoutManager.HORIZONTAL
//        recycleView.setOnItemListener(this)
//        recycleView.setOnItemClickListener { parent, itemView, position ->
//
//        }
        recycleView.setLayoutManager(gridlayoutManager)
        recycleView.setFocusable(false)
        recycleView.setSelectedItemAtCentered(true) // 设置item在中间移动.
        recycleView.setAdapter(adapter)
        recycleView.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
                val space = 5
                outRect?.left = space
                outRect?.right = space
                outRect?.bottom = space

                // Add top margin only for the first item to avoid double space between items
                if (parent?.getChildPosition(view) == 0)
                    outRect?.top = space
            }
        })

        recycleView.setRecycleCall(object : TVRecyclerView.RecycleCall() {
            override fun onItemSelect(position: Int, view: View) {
                println("$position is Select")
                val view = (view as ViewGroup)
                (0..view.childCount).map { view.getChildAt(it) }.filter { it is TextView }.forEach {
                    (it as TVTextView).startMarquee()
                }
            }

            override fun onItemUnSelect(position: Int, view: View) {
                val view = (view as ViewGroup)
                (0..view.childCount).map { view.getChildAt(it) }.filter { it is TextView }.forEach {
                    (it as TVTextView).stopMarquee()
                }
            }

            override fun onItemClick(position: Int, view: View) {
                println("$position is Click")
            }
        })
//        recycleView.postDelayed({
//            recycleView.setDefaultSelect(95)
//        }, 2000)

        with(rootLay.viewTreeObserver) {
            addOnGlobalFocusChangeListener { oldFocus, newFocus ->
                println("addOnGlobalFocusChangeListener")
                if (newFocus is RecyclerView) {

                } else {
                    newFocus?.let {
                        it.bringToFront()
                        focusView.setFocusView(it)
                    }
                }
            }
            addOnScrollChangedListener {
                println("addOnScrollChangedListener")
                rootLay.findFocus()?.let {
                    it.bringToFront()
                    focusView.setFocusView(it)
                }
            }
        }

        /**
         * 4.0.4系统上测试，addOnScrollChangedListener失效，需要加载滚动监听！
         */
        recycleView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                rootLay.findFocus()?.let {
                    focusView.setFocusView(it)
                }
            }
        })
//        recycleView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
//                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
//                    //滚动结束
//                    rootLay.findFocus()?.let { focusView.setFocusView(it) }
//                }
//            }
//        })

//        focusView.setBaseAnimator(MoveFocusAnimator())

        verticalBtn.setOnClickListener(this)
        verticalBtn.setOnKey {
            when (it) {
                KeyEvent.KEYCODE_DPAD_RIGHT -> {
                    recycleView.setDefaultSelect(96)
                    true
                }
                else -> false
            }
        }
        recycleView.setOnKey {
            when (it) {
                KeyEvent.KEYCODE_DPAD_RIGHT -> {
                    horizontalBtn.requestFocus()
                    true
                }
                KeyEvent.KEYCODE_DPAD_LEFT -> {
                    verticalBtn.requestFocus()
                    true
                }
                KeyEvent.KEYCODE_DPAD_DOWN -> {
                    verticalx2Btn.requestFocus()
                    true
                }
                else -> false
            }
        }

        horizontalBtn.setOnClickListener(this)
        horizontalBtn.setOnKey {
            when (it) {
                KeyEvent.KEYCODE_DPAD_RIGHT -> {
                    recycleView.setDefaultSelect(1)
                    true
                }
                else -> false
            }
        }

        focusView.setBaseAnimator(MoveFocusAnimator())
        focusView.setScale(1.1f, 150L)

        verticalx2Btn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            verticalBtn -> {
                val gridlayoutManager = LinearLayoutManager(this)
                gridlayoutManager.orientation = LinearLayoutManager.VERTICAL
                recycleView.layoutManager = gridlayoutManager
                recycleView?.adapter = recycleView?.adapter
            }
            horizontalBtn -> {
                val gridlayoutManager = LinearLayoutManager(this)
                gridlayoutManager.orientation = LinearLayoutManager.HORIZONTAL
                recycleView.layoutManager = gridlayoutManager
                recycleView?.adapter = recycleView?.adapter
            }
            verticalx2Btn -> {
                val gridlayoutManager = GridLayoutManager(this, 5)
                gridlayoutManager.orientation = GridLayoutManager.VERTICAL
                recycleView.layoutManager = gridlayoutManager
                recycleView?.adapter = recycleView?.adapter
            }

            else -> {
            }
        }
    }
}