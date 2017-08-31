package xyz.marcb.rxadapter

import android.support.v7.widget.RecyclerView
import rx.Observable
import java.util.*

class StaticItem<VH>(private val vhClass: Class<VH>)
    : AdapterPart where VH : RecyclerView.ViewHolder {

    var binder: ((VH) -> Unit)? = null
    override var visible: Observable<Boolean>? = null

    private val id = UUID.randomUUID().toString()

    override val snapshots: Observable<AdapterPartSnapshot> get() =
        Observable.just(Snapshot(vhClass, listOf(id), listOf(id), { _, vh -> binder?.invoke(vh) }))
}
