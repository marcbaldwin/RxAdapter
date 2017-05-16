package xyz.marcb.rxadapter

import android.support.v7.widget.RecyclerView
import rx.Observable

class Items<I, VH: RecyclerView.ViewHolder>(
        val vhClass: Class<VH>,
        val items: Observable<List<I>>)
    : AdapterPart {

    var binder: ((I, VH) -> Unit)? = null
    override var visible: Observable<Boolean>? = null

    override val snapshots: Observable<AdapterPartSnapshot> get() {
        return items.map { items -> ItemsSnapshot(items) }
    }

    internal inner class ItemsSnapshot(val items: List<I>)
        : AdapterPartSnapshot {

        override val itemCount: Int = items.count()

        override fun viewHolderClass(index: Int): Class<VH> = vhClass

        @Suppress("UNCHECKED_CAST")
        override fun bind(viewHolder: RecyclerView.ViewHolder, index: Int) {
            val item = items[index]
            binder?.invoke(item, viewHolder as VH)
        }
    }
}
