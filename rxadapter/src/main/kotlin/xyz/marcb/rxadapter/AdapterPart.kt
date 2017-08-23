package xyz.marcb.rxadapter

import android.support.v7.widget.RecyclerView
import rx.Observable

interface AdapterPart {

    val snapshots: Observable<AdapterPartSnapshot>
    val visible: Observable<Boolean>?
}

interface AdapterPartSnapshot {

    val itemIds: List<String>
    val itemCount: Int get() = itemIds.size

    fun viewHolderClass(index: Int): Class<out RecyclerView.ViewHolder>
    fun bind(viewHolder: RecyclerView.ViewHolder, index: Int)
    fun underlyingObject(index: Int): Any
}

internal fun AdapterPart.compose(): Observable<AdapterPartSnapshot> {
    val visible = visible?.let { it } ?: return snapshots
    return visible.switchMap { isVisible ->
        if (isVisible) snapshots else Observable.just(EmptySnapshot())
    }
}

internal fun List<AdapterPart>.combine(): Observable<AdapterPartSnapshot> {
    return Observable.combineLatest(map(AdapterPart::compose)) { snapshots ->
        CompositeAdapterPartSnapshot(snapshots.map { snapshot -> snapshot as AdapterPartSnapshot })
    }
}

internal class EmptySnapshot : AdapterPartSnapshot {

    override val itemIds: List<String> = emptyList()

    override fun viewHolderClass(index: Int): Class<out RecyclerView.ViewHolder> {
        error("Internal error: Attempted to get view holder class from an empty snapshot")
    }

    override fun bind(viewHolder: RecyclerView.ViewHolder, index: Int) {
        error("Internal error: Attempted to bind view holder to an empty snapshot")
    }

    override fun underlyingObject(index: Int): Any {
        error("Internal error: Attempted to get underlying object of an empty snapshot")
    }
}