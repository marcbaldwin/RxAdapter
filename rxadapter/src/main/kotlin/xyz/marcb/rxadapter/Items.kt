package xyz.marcb.rxadapter

import androidx.recyclerview.widget.RecyclerView
import rx.Observable
import java.util.*

class Items<I, VH>(
        private val vhClass: Class<VH>,
        private val items: Observable<List<I>>
) : AdapterPart where I: Any, VH: RecyclerView.ViewHolder {

    var binder: (VH.(I) -> Unit)? = null
    var onClick: (VH.(I) -> Unit)? = null
    var id: ((I) -> String)? = null
    override var visible: Observable<Boolean>? = null

    override val snapshots: Observable<AdapterPartSnapshot> get() =
        items.map { items ->
            val ids = items.map { id?.invoke(it) ?: UUID.randomUUID().toString() }
            Snapshot(vhClass, items, ids, binder, onClick)
        }
}
