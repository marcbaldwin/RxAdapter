package xyz.marcb.rxadapter

import android.support.v7.widget.RecyclerView
import rx.Observable

class Section: AdapterPart {

    override val snapshots: Observable<AdapterPartSnapshot> get() = parts.combine()

    override var visible: Observable<Boolean>? = null

    private val parts = ArrayList<AdapterPart>()

    fun <VH: RecyclerView.ViewHolder> addItem(vhClass: Class<VH>): Item<VH> {
        return add(Item(vhClass))
    }

    fun <I, VH: RecyclerView.ViewHolder> addItem(vhClass: Class<VH>, item: Observable<I?>): OptionalItem<I, VH> {
        return add(OptionalItem(vhClass, item))
    }

    fun <I, VH: RecyclerView.ViewHolder> addItems(vhClass: Class<VH>, items: List<I>): Items<I, VH> {
        return addItems(vhClass, Observable.just(items))
    }

    fun <I, VH: RecyclerView.ViewHolder> addItems(vhClass: Class<VH>, items: Observable<List<I>>): Items<I, VH> {
        return add(Items(vhClass, items))
    }

    internal fun <R: AdapterPart> add(part: R): R {
        parts.add(part)
        return part
    }
}