package xyz.marcb.rxadapter

import android.support.v7.widget.RecyclerView
import rx.Observable

class OptionalItem<I, VH: RecyclerView.ViewHolder>(val vhClass: Class<VH>, val item: Observable<I?>) : AdapterPart {

    var binder: ((I, VH) -> Unit)? = null
    override var visible: Observable<Boolean>? = null

    override val snapshots: Observable<AdapterPartSnapshot> get() {
        return item.map { item ->
            item?.let { Snapshot(it) } ?: EmptySnapshot()
        }
    }

    internal inner class Snapshot(val item: I): AdapterPartSnapshot {

        override val itemCount: Int = 1

        override fun viewHolderClass(index: Int): Class<VH> = vhClass

        @Suppress("UNCHECKED_CAST")
        override fun bind(viewHolder: RecyclerView.ViewHolder, index: Int) {
            binder?.invoke(item, viewHolder as VH)
        }
    }
}