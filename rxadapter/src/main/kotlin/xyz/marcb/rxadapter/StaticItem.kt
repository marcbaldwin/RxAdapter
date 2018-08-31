package xyz.marcb.rxadapter

import androidx.recyclerview.widget.RecyclerView
import rx.Observable
import java.util.*

class StaticItem<VH>(private val vhClass: Class<VH>)
    : AdapterPart where VH : RecyclerView.ViewHolder {

    var binder: (VH.() -> Unit)? = null
    var onClick: (VH.() -> Unit)? = null
    override var visible: Observable<Boolean>? = null

    private val id = UUID.randomUUID().toString()

    override val snapshots: Observable<AdapterPartSnapshot> get() {
        val binderTransformer: (VH.(String) -> Unit)? = binder?.let { binder -> { binder.invoke(this) } }
        val onClickTransformer: (VH.(String) -> Unit)? = onClick?.let { onClick -> { onClick.invoke(this) } }
        return Observable.just(
                Snapshot(vhClass, listOf(id), listOf(id), binderTransformer, onClickTransformer)
        )
    }
}
