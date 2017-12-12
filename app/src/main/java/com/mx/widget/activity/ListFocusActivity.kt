package com.mx.widget.activity

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.AbsListView
import android.widget.AdapterView
import com.mx.widget.R
import com.mx.widget.adapts.MyListAdapt
import com.mx.widget.animator.MoveFocusAnimator
import kotlinx.android.synthetic.main.list_focus_activity.*


/**
 * Created by ZMX on 2017/12/11.
 */
class ListFocusActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.list_focus_activity)

        rootLay.viewTreeObserver.addOnGlobalFocusChangeListener { oldFocus, newFocus ->
            if (newFocus is AbsListView) {
                newFocus.addOnLayoutChangeListener(object : View.OnLayoutChangeListener {
                    override fun onLayoutChange(v: View?, left: Int, top: Int, right: Int, bottom: Int, oldLeft: Int, oldTop: Int, oldRight: Int, oldBottom: Int) {
                        newFocus.removeOnLayoutChangeListener(this)
                        try {
                            focusView.setFocusView(newFocus.selectedView)
                        } catch (ex: Exception) {
                            ex.printStackTrace()
                        }
                    }
                })
                newFocus.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        focusView.visibility = View.GONE
                    }

                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        view?.let { focusView.setFocusView(it) }
                    }
                }

            } else {
                focusView.setFocusView(newFocus)
            }
        }

        val list = ArrayList<String>()
        (0..1000).forEach {
            list.add(it.toString())
        }
        val adapter = MyListAdapt(list)
        listView.adapter = adapter

        focusView.setBaseAnimator(MoveFocusAnimator())
    }
}