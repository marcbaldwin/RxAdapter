package xyz.marcb.rxadapter

import android.support.v7.widget.RecyclerView
import rx.Observable
import java.util.*

class OptionalItem<I: Any, VH: RecyclerView.ViewHolder>(
        val vhClass: Class<VH>, private val item: Observable<I?>)
    : AdapterPart {

    var binder: ((I, VH) -> Unit)? = null
    override var visible: Observable<Boolean>? = null

    private val id = UUID.randomUUID().toString()

    override val snapshots: Observable<AdapterPartSnapshot> get() {
        return item.map { item ->
            item?.let { Snapshot(it) } ?: EmptySnapshot()
        }
    }

    internal inner class Snapshot(private val item: I): AdapterPartSnapshot {

        override val itemIds: List<String> = listOf(id)

        override fun viewHolderClass(index: Int): Class<VH> = vhClass

        @Suppress("UNCHECKED_CAST")
        override fun bind(viewHolder: RecyclerView.ViewHolder, index: Int) {
            binder?.invoke(item, viewHolder as VH)
        }

        override fun underlyingObject(index: Int): Any = item
    }
}