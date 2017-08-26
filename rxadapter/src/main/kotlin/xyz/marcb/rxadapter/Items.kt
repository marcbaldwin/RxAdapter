package xyz.marcb.rxadapter

import android.support.v7.widget.RecyclerView
import rx.Observable
import java.util.*

class Items<I: Any, VH: RecyclerView.ViewHolder>(
        val vhClass: Class<VH>,
        private val items: Observable<List<I>>)
    : AdapterPart {

    var binder: ((I, VH) -> Unit)? = null
    var id: ((I) -> String)? = null
    override var visible: Observable<Boolean>? = null

    override val snapshots: Observable<AdapterPartSnapshot> get() =
        items.map { items -> ItemsSnapshot(items) }

    internal inner class ItemsSnapshot(private val items: List<I>) : AdapterPartSnapshot {

        override val itemIds: List<String> = items.map { id?.invoke(it) ?: UUID.randomUUID().toString() }

        override fun viewHolderClass(index: Int): Class<VH> = vhClass

        @Suppress("UNCHECKED_CAST")
        override fun bind(viewHolder: RecyclerView.ViewHolder, index: Int) {
            binder?.invoke(items[index], viewHolder as VH)
        }

        override fun underlyingObject(index: Int): Any = items[index]
    }
}
