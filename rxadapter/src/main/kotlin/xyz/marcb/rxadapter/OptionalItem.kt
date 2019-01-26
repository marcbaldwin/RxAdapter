package xyz.marcb.rxadapter

import androidx.recyclerview.widget.RecyclerView
import rx.Observable

class OptionalItem<I, VH>(
        private val vhClass: Class<VH>,
        private val item: Observable<I?>,
        private val id: Long = RecyclerView.NO_ID
) : AdapterPart where I: Any, VH: RecyclerView.ViewHolder {

    var binder: (VH.(I) -> Unit)? = null
    var onClick: (VH.(I) -> Unit)? = null
    override var visible: Observable<Boolean>? = null

    override val snapshots: Observable<AdapterPartSnapshot> get() =
        item.map { item ->
            item?.let { Snapshot(vhClass, listOf(it), listOf(id), binder, onClick) } ?: EmptySnapshot()
        }
}