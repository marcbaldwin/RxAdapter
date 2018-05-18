package xyz.marcb.rxadapter

import android.support.v7.widget.RecyclerView
import io.reactivex.Observable
import java.util.UUID

class OptionalItem<I, VH>(
        private val vhClass: Class<VH>,
        private val item: Observable<Optional<I>>
) : AdapterPart where I: Any, VH: RecyclerView.ViewHolder {

    var binder: (VH.(I) -> Unit)? = null
    var onClick: (VH.(I) -> Unit)? = null
    override var visible: Observable<Boolean>? = null
    private val id = UUID.randomUUID().toString()

    override val snapshots: Observable<AdapterPartSnapshot> get() =
        item.map { item ->
            when (item) {
                is Optional.Some -> Snapshot(vhClass, listOf(item.item), listOf(id), binder, onClick)
                is Optional.None -> EmptySnapshot()
            }
        }

}
