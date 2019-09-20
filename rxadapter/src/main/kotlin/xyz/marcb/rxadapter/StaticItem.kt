package xyz.marcb.rxadapter

import androidx.recyclerview.widget.RecyclerView
import io.reactivex.Observable
import xyz.marcb.rxadapter.internal.Snapshot

class StaticItem<VH>(
        private val vhClass: Class<VH>,
        private val id: Long = RecyclerView.NO_ID
) : AdapterPart where VH : RecyclerView.ViewHolder {

    var binder: (VH.() -> Unit)? = null
    var onClick: (VH.() -> Unit)? = null
    override var visible: Observable<Boolean>? = null

    override val snapshots: Observable<AdapterPartSnapshot>
        get() {
            val binderTransformer: (VH.(Long) -> Unit)? = binder?.let { binder -> { binder.invoke(this) } }
            val onClickTransformer: (VH.(Long) -> Unit)? = onClick?.let { onClick -> { onClick.invoke(this) } }
            return Observable.just(
                    Snapshot(vhClass, listOf(id), listOf(id), binderTransformer, onClickTransformer)
            )
        }
}
