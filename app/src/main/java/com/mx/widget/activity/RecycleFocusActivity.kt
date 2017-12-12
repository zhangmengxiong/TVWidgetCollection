package com.mx.widget.activity

import android.app.Activity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.KeyEvent
import android.view.View
import com.mx.widget.R
import com.mx.widget.adapts.MyRecycleAdapt
import com.mx.widget.animator.MoveFocusAnimator
import com.mx.widget.views.RecycleCall
import com.mx.widget.views.RecyclerViewTV
import com.mx.widget.views.setOnKey
import kotlinx.android.synthetic.main.recycle_focus_activity.*

/**
 * Created by ZMX on 2017/12/11.
 */
class RecycleFocusActivity : Activity(), RecyclerViewTV.OnItemListener {
    override fun onItemPreSelected(parent: RecyclerViewTV?, itemView: View?, position: Int) {
        itemView?.let { focusView.setFocusView(it) }
    }

    override fun onItemSelected(parent: RecyclerViewTV?, itemView: View?, position: Int) {
        itemView?.let { focusView.setFocusView(it) }
    }

    override fun onReviseFocusFollow(parent: RecyclerViewTV?, itemView: View?, position: Int) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.recycle_focus_activity)

        val list = ArrayList<String>()
        (0..100).forEach {
            list.add(it.toString())
        }

        val adapter = MyRecycleAdapt(list)
        val gridlayoutManager = LinearLayoutManager(this) // 解决快速长按焦点丢失问题.
        gridlayoutManager.orientation = LinearLayoutManager.HORIZONTAL
//        recycleView.setOnItemListener(this)
//        recycleView.setOnItemClickListener { parent, itemView, position ->
//
//        }
        recycleView.setLayoutManager(gridlayoutManager)
        recycleView.setFocusable(false)
//        recycleView.setSelectedItemAtCentered(true) // 设置item在中间移动.
        recycleView.setAdapter(adapter)

        recycleView.setRecycleCall(object : RecycleCall() {
            override fun onItemSelect(position: Int, view: View) {
                println("$position is Select")
            }

            override fun onItemClick(position: Int, view: View) {
                println("$position is Click")
            }

            override fun onKeyDown(): Boolean {
                println("向下焦点")
                return true
            }
        })
        recycleView.postDelayed({
            recycleView.setDefaultSelect(95)
        }, 2000)

//        recycleView.setSelectedItemOffset(111, 111)
        recycleView.setSelectedItemAtCentered(true)

        with(rootLay.viewTreeObserver) {
            addOnGlobalFocusChangeListener { oldFocus, newFocus ->
                if (newFocus is RecyclerView) {

                } else {
                    focusView.setFocusView(newFocus)
                }
            }
            addOnScrollChangedListener {
                rootLay.findFocus()?.let { focusView.setFocusView(it) }
            }
        }

        focusView.setBaseAnimator(MoveFocusAnimator())

        btn.setOnKey {
            when (it) {
                KeyEvent.KEYCODE_DPAD_RIGHT -> {
                    recycleView.setDefaultSelect(recycleView.findFirstVisibleItemPosition())
                    true
                }
                else -> false
            }

        }
    }
}