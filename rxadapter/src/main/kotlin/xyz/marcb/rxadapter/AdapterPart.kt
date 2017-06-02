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
}

internal fun AdapterPart.compose(): Observable<AdapterPartSnapshot> {
    val visible = visible?.let { it } ?: return snapshots
    return visible.switchMap { visible ->
        if (visible) snapshots else Observable.just(EmptySnapshot())
    }
}

internal fun List<AdapterPart>.combine(): Observable<AdapterPartSnapshot> {
    val snapshots: List<Observable<AdapterPartSnapshot>> = map { part ->
        part.compose()
    }
    return Observable.combineLatest(snapshots) { snapshots ->
        CompositeAdapterPartSnapshot(snapshots.map { it as AdapterPartSnapshot })
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
}