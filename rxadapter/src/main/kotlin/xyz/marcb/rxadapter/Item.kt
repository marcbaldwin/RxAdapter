package xyz.marcb.rxadapter

import android.support.v7.widget.RecyclerView
import rx.Observable
import java.util.*

class Item<VH: RecyclerView.ViewHolder>(val vhClass: Class<VH>) : AdapterPart {

    var binder: ((VH) -> Unit)? = null
    override var visible: Observable<Boolean>? = null

    private val id = UUID.randomUUID().toString()

    override val snapshots: Observable<AdapterPartSnapshot> get() {
        return Observable.just(ItemSnapshot())
    }

    internal inner class ItemSnapshot: AdapterPartSnapshot {

        override val itemIds: List<String> = listOf(id)

        override fun viewHolderClass(index: Int): Class<VH> = vhClass

        @Suppress("UNCHECKED_CAST")
        override fun bind(viewHolder: RecyclerView.ViewHolder, index: Int) {
            binder?.invoke(viewHolder as VH)
        }

        override fun underlyingObject(index: Int): Any = this
    }
}
