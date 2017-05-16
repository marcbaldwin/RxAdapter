package xyz.marcb.rxadapter

import android.support.v7.widget.RecyclerView
import rx.Observable

class Item<VH: RecyclerView.ViewHolder>(val vhClass: Class<VH>) : AdapterPart {

    var binder: ((VH) -> Unit)? = null
    override var visible: Observable<Boolean>? = null

    override val snapshots: Observable<AdapterPartSnapshot> get() {
        return Observable.just(ItemSnapshot())
    }

    internal inner class ItemSnapshot: AdapterPartSnapshot {

        override val itemCount: Int = 1

        override fun viewHolderClass(index: Int): Class<VH> = vhClass

        @Suppress("UNCHECKED_CAST")
        override fun bind(viewHolder: RecyclerView.ViewHolder, index: Int) {
            binder?.invoke(viewHolder as VH)
        }
    }
}
