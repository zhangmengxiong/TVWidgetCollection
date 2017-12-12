package com.mx.widget.adapts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mx.widget.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.my_list_layout.*

/**
 * Created by ZMX on 2017/12/11.
 */
class MyRecycleAdapt(list: ArrayList<String>) : BaseRecycleAdapt<String>(list) {
    override fun createItem(parent: ViewGroup): View {
        return LayoutInflater.from(parent.context).inflate(R.layout.my_list_layout, parent, false)
    }

    override fun bindView(position: Int, holder: LayoutContainer, record: String) {
        holder.textView.text = record
    }
}