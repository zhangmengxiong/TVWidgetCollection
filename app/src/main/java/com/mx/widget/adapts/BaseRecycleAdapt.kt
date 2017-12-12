package com.mx.widget.adapts

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import kotlinx.android.extensions.LayoutContainer
import java.util.*

/**
 * Created by ZMX on 2017/12/11.
 */

abstract class BaseRecycleAdapt<T>(private val list: ArrayList<T>) : RecyclerView.Adapter<BaseRecycleAdapt.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = createItem(parent)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        bindView(position, holder, item)
    }

    protected fun getItem(position: Int): T = list[position]

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(override val containerView: View?) : RecyclerView.ViewHolder(containerView), LayoutContainer

    protected abstract fun createItem(parent: ViewGroup): View

    protected abstract fun bindView(position: Int, holder: LayoutContainer, record: T)
}
