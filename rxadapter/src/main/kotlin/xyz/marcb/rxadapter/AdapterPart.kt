package xyz.marcb.rxadapter

import androidx.recyclerview.widget.RecyclerView
import io.reactivex.Observable
import xyz.marcb.rxadapter.internal.CompositeAdapterPartSnapshot
import xyz.marcb.rxadapter.internal.EmptySnapshot

interface AdapterPart {

    val snapshots: Observable<AdapterPartSnapshot>
    val visible: Observable<Boolean>?
}

interface AdapterPartSnapshot {

    val itemIds: List<Long>
    val itemCount: Int get() = itemIds.size

    fun viewHolderClass(index: Int): Class<out RecyclerView.ViewHolder>
    fun bind(viewHolder: RecyclerView.ViewHolder, index: Int)
    fun underlyingObject(index: Int): Any?
}

internal fun AdapterPart.compose(): Observable<AdapterPartSnapshot> {
    val visible = visible ?: return snapshots
    return visible.switchMap { isVisible ->
        if (isVisible) snapshots else Observable.just(EmptySnapshot())
    }
}

internal fun List<AdapterPart>.combine(): Observable<AdapterPartSnapshot> {
    return Observable.combineLatest(map(AdapterPart::compose)) { snapshots ->
        CompositeAdapterPartSnapshot(
            snapshots.map { snapshot -> snapshot as AdapterPartSnapshot }
        )
    }
}
