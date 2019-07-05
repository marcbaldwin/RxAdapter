package xyz.marcb.rxadapter.internal

import androidx.recyclerview.widget.RecyclerView
import xyz.marcb.rxadapter.AdapterPartSnapshot

internal class CompositeAdapterPartSnapshot(val parts: List<AdapterPartSnapshot>) : AdapterPartSnapshot {

    override val itemIds: List<Long> = parts.flatMap { it.itemIds }

    override fun viewHolderClass(index: Int): Class<out RecyclerView.ViewHolder> {
        val (adapter, adjustedIndex) = adapterWithAdjustedIndex(index)
        return adapter.viewHolderClass(index = adjustedIndex)
    }

    override fun bind(viewHolder: RecyclerView.ViewHolder, index: Int) {
        val (adapter, adjustedIndex) = adapterWithAdjustedIndex(index)
        adapter.bind(viewHolder, index = adjustedIndex)
    }

    override fun underlyingObject(index: Int): Any? {
        val (adapter, adjustedIndex) = adapterWithAdjustedIndex(index)
        return adapter.underlyingObject(index = adjustedIndex)
    }

    private fun adapterWithAdjustedIndex(index: Int): Pair<AdapterPartSnapshot, Int> {
        var currentIndex = 0
        for (part in parts) {
            currentIndex += part.itemCount
            if (currentIndex > index) {
                return Pair(part, index - (currentIndex - part.itemCount))
            }
        }
        throw IllegalArgumentException("Internal error: Index exceeded item count")
    }
}