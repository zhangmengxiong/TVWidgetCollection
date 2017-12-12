package com.mx.widget.adapts

import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.extensions.LayoutContainer
import java.util.*

/**
 * 加油列表adapt
 * 创建人： zhangmengxiong
 * 创建时间： 2017/5/15.
 * 联系方式: zmx_final@163.com
 */
abstract class BaseListAdapt<T>(private val list: ArrayList<T>) : BaseAdapter() {
    override fun getCount(): Int = list.size

    override fun getItem(position: Int): T = list[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val record = getItem(position)
        var holder: ViewHolder? = null
        if (convertView == null) {
            convertView = createItem(parent)
            holder = ViewHolder(convertView)
            convertView.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }
        bindView(position, holder, record)
        return convertView
    }

    protected class ViewHolder(override val containerView: View?) : LayoutContainer

    protected abstract fun createItem(parent: ViewGroup): View

    protected abstract fun bindView(position: Int, holder: LayoutContainer, record: T)
}
