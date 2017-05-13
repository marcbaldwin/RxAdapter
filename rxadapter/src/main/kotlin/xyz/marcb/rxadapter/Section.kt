package xyz.marcb.rxadapter

import android.support.v7.widget.RecyclerView
import rx.Observable

class Section: AdapterPart {

    private val parts = ArrayList<AdapterPart>()

    fun <VH: RecyclerView.ViewHolder> addItem(vhClass: Class<VH>): Item<VH> {
        return add(Item(vhClass))
    }

    fun <I, VH: RecyclerView.ViewHolder> addItems(vhClass: Class<VH>, items: Observable<List<I>>): Items<I, VH> {
        return add(Items(vhClass, items))
    }

    // AdapterPart impl

    override val snapshots: Observable<AdapterPartSnapshot> get() {
        return Observable.combineLatest(parts.map { it.snapshots }) { snapshots ->
            CompositeAdapterPart(snapshots.map { it as AdapterPartSnapshot })
        }
    }

    // Private

    private fun <R: AdapterPart> add(part: R): R {
        parts.add(part)
        return part
    }
}