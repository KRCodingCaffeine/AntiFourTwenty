package com.arista.antifourtwenty.features.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.arista.antifourtwenty.R

class GridAdapter(private val mContext: Context, private val menuCount: Array<String>) : BaseAdapter() {

    override fun getCount(): Int {
        return menuCount.size
    }

    override fun getItem(position: Int): Any? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val grid: View
        val inflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        if (convertView == null) {
            grid = inflater.inflate(R.layout.grid_layout, parent, false)
            val countText = grid.findViewById<TextView>(R.id.grid_op_text)
            countText.text = menuCount[position]
        } else {
            grid = convertView
        }
        return grid
    }
}