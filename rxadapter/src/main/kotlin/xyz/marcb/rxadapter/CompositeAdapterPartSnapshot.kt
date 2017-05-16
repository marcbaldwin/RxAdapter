package xyz.marcb.rxadapter

import android.support.v7.widget.RecyclerView

internal class CompositeAdapterPartSnapshot(val parts: List<AdapterPartSnapshot>) : AdapterPartSnapshot {

    override val itemCount: Int get() {
        return parts.sumBy { it.itemCount }
    }

    override fun viewHolderClass(index: Int): Class<out RecyclerView.ViewHolder> {
        val adapterWithIndex = adapterWithAdjustedIndex(index)
        return adapterWithIndex.first.viewHolderClass(adapterWithIndex.second)
    }

    override fun bind(viewHolder: RecyclerView.ViewHolder, index: Int) {
        val adapterWithIndex = adapterWithAdjustedIndex(index)
        adapterWithIndex.first.bind(viewHolder, index = adapterWithIndex.second)
    }

    internal fun adapterWithAdjustedIndex(index: Int): Pair<AdapterPartSnapshot, Int> {
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