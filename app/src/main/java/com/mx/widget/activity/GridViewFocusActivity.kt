package com.mx.widget.activity

import android.app.Activity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.AbsListView
import android.widget.AdapterView
import com.mx.widget.R
import com.mx.widget.adapts.MyListAdapt
import com.mx.widget.animator.MoveFocusAnimator
import com.mx.widget.utils.setOnKey
import com.mx.widget.views.TVScrollCall
import kotlinx.android.synthetic.main.gridview_focus_activity.*


/**
 * ListView 的Item无法获取焦点，但是可以通过setSelectItem()来设置选中项
 * 1：监听全局addOnGlobalFocusChangeListener 焦点变化监听
 * 2：监听全局addOnScrollChangedListener 滚动变化监听
 * 3：监听ListView：setOnItemSelectedListener 监听ListView选中变化
 *
 * 需要注意的点：
 * 1：ListView的SelectedView可能为空
 * 2：bringToFront() 方法会带来焦点乱跳的问题
 *
 * Created by ZMX on 2017/12/11.
 */
class GridViewFocusActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.gridview_focus_activity)

        rootLay.viewTreeObserver.addOnGlobalFocusChangeListener { oldFocus, newFocus ->
            if (newFocus is AbsListView) {
                newFocus.addOnLayoutChangeListener(object : View.OnLayoutChangeListener {
                    override fun onLayoutChange(v: View?, left: Int, top: Int, right: Int,
                                                bottom: Int, oldLeft: Int, oldTop: Int, oldRight: Int, oldBottom: Int) {
                        newFocus.removeOnLayoutChangeListener(this)
                        newFocus.selectedView?.let {
                            focusView.setFocusView(it)
                        }
                    }
                })
            } else {
                focusView.setFocusView(newFocus)
            }
        }
        rootLay.viewTreeObserver.addOnScrollChangedListener {
            val newFocus = rootLay.findFocus() ?: return@addOnScrollChangedListener
            if (newFocus is AbsListView) {
                newFocus.addOnLayoutChangeListener(object : View.OnLayoutChangeListener {
                    override fun onLayoutChange(v: View?, left: Int, top: Int, right: Int,
                                                bottom: Int, oldLeft: Int, oldTop: Int, oldRight: Int, oldBottom: Int) {
                        newFocus.removeOnLayoutChangeListener(this)
                        newFocus.selectedView?.let { focusView.setFocusView(it) }
                    }
                })
            } else {
                focusView.setFocusView(newFocus)
            }
        }

        val list = ArrayList<String>()
        (0..100).forEach {
            list.add(it.toString())
        }
        val adapter = MyListAdapt(list)
        tvGridView.adapter = adapter
        tvGridView.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
//                        focusView.visibility = View.GONE
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                view?.let {
                    it.bringToFront()
                    focusView.setFocusView(it)
                }
            }
        }
//        tvGridView.setFocusCenterInViewGroup(true)

        /**
         * 添加对ListView的滚动监听，防止出现焦点错位
         */
        tvGridView.setOnTVScrollListener(object : TVScrollCall {
            override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
                if (tvGridView.hasFocus()) {
                    tvGridView.selectedView?.let { focusView.setFocusView(it) }
                }
            }
        })
        focusView.setBaseAnimator(MoveFocusAnimator())
        focusView.setScale(1.05f, 150)
        btn.setOnKey {
            when (it) {
                KeyEvent.KEYCODE_DPAD_RIGHT -> {
                    tvGridView.setDefaultSelect(98)
                    tvGridView.post { tvGridView.requestFocus() }
                    true
                }
                else -> {
                    false
                }
            }
        }
    }
}