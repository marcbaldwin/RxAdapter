package xyz.marcb.rxadapter

import androidx.recyclerview.widget.RecyclerView
import android.view.View

abstract class HeaderViewHolder(view: View): RecyclerView.ViewHolder(view) {
    abstract fun bind(input: String?)
}
