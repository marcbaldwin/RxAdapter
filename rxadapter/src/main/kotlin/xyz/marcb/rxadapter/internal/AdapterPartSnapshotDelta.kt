package xyz.marcb.rxadapter.internal

import androidx.recyclerview.widget.DiffUtil
import xyz.marcb.rxadapter.AdapterPartSnapshot

internal class AdapterPartSnapshotDelta(
        private val old: AdapterPartSnapshot,
        private val new: AdapterPartSnapshot
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = old.itemCount
    override fun getNewListSize(): Int = new.itemCount

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            old.itemIds[oldItemPosition] == new.itemIds[newItemPosition]

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            old.underlyingObject(oldItemPosition) == new.underlyingObject(newItemPosition)
}