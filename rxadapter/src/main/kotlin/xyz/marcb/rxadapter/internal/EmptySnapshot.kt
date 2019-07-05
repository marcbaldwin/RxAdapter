package xyz.marcb.rxadapter.internal

import androidx.recyclerview.widget.RecyclerView
import xyz.marcb.rxadapter.AdapterPartSnapshot

internal class EmptySnapshot : AdapterPartSnapshot {

    override val itemIds: List<Long> = emptyList()

    override fun viewHolderClass(index: Int): Class<out RecyclerView.ViewHolder> {
        error("Internal error: Attempted to get view holder class from an empty snapshot")
    }

    override fun bind(viewHolder: RecyclerView.ViewHolder, index: Int) {
        error("Internal error: Attempted to bind view holder to an empty snapshot")
    }

    override fun underlyingObject(index: Int): Any {
        error("Internal error: Attempted to get underlying object of an empty snapshot")
    }
}