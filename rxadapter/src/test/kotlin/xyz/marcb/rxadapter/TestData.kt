package xyz.marcb.rxadapter

import android.support.v7.widget.RecyclerView
import android.view.View

abstract class HeaderViewHolder(view: View): RecyclerView.ViewHolder(view) {
    abstract fun bind(input: String)
}
