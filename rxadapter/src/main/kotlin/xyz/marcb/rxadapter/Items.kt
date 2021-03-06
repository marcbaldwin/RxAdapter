package xyz.marcb.rxadapter

import androidx.recyclerview.widget.RecyclerView
import io.reactivex.Observable
import xyz.marcb.rxadapter.internal.Snapshot

class Items<I, VH>(
    private val vhClass: Class<VH>,
    private val items: Observable<List<I>>
) : AdapterPart where VH : RecyclerView.ViewHolder {

    var binder: (VH.(I) -> Unit)? = null
    var onClick: (VH.(I) -> Unit)? = null
    var id: ((I) -> Long)? = null
    override var visible: Observable<Boolean>? = null

    override val snapshots: Observable<AdapterPartSnapshot>
        get() = items.map { items ->
            val ids = items.map { id?.invoke(it) ?: RecyclerView.NO_ID }
            Snapshot(vhClass, items, ids, binder, onClick)
        }
}
