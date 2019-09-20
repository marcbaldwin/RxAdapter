package xyz.marcb.rxadapter

import androidx.recyclerview.widget.RecyclerView
import io.reactivex.Observable
import xyz.marcb.rxadapter.internal.EmptySnapshot
import xyz.marcb.rxadapter.internal.Snapshot

class OptionalItem<O, I, VH>(
        private val vhClass: Class<VH>,
        private val item: Observable<O>,
        private val unwrap: (O) -> I?,
        private val id: Long = RecyclerView.NO_ID
) : AdapterPart where VH : RecyclerView.ViewHolder {

    var binder: (VH.(I) -> Unit)? = null
    var onClick: (VH.(I) -> Unit)? = null
    override var visible: Observable<Boolean>? = null

    override val snapshots: Observable<AdapterPartSnapshot>
        get() = item.map { item ->
            unwrap(item)
                    ?.let { Snapshot(vhClass, listOf(it), listOf(id), binder, onClick) }
                    ?: EmptySnapshot()
        }
}