package xyz.marcb.rxadapter

import android.support.v7.widget.RecyclerView
import rx.Observable

class Section: AdapterPart {

    override val snapshots: Observable<AdapterPartSnapshot> get() = parts.combine()

    override var visible: Observable<Boolean>? = null

    private val parts = ArrayList<AdapterPart>()

    fun <VH> item(vhClass: Class<VH>, init: (StaticItem<VH>.() -> Unit)? = null): StaticItem<VH>
            where VH: RecyclerView.ViewHolder {
        val part = add(StaticItem(vhClass))
        init?.invoke(part)
        return part
    }

    fun <I, VH> item(vhClass: Class<VH>, item: Observable<I>, init: (Item<I, VH>.() -> Unit)? = null): Item<I, VH>
            where I: Any?, VH: RecyclerView.ViewHolder {
        val part = add(Item(vhClass, item))
        init?.invoke(part)
        return part
    }

    fun <I, VH> optionalItem(vhClass: Class<VH>, item: Observable<I?>, init: (OptionalItem<I, VH>.() -> Unit)? = null): OptionalItem<I, VH>
            where I: Any, VH: RecyclerView.ViewHolder {
        val part = add(OptionalItem(vhClass, item))
        init?.invoke(part)
        return part
    }

    fun <I, VH> items(vhClass: Class<VH>, items: List<I>, init: (Items<I, VH>.() -> Unit)? = null): Items<I, VH> where I: Any, VH: RecyclerView.ViewHolder =
            items(vhClass, Observable.just(items), init)

    fun <I, VH> items(vhClass: Class<VH>, items: Observable<List<I>>, init: (Items<I, VH>.() -> Unit)? = null): Items<I, VH>
            where I: Any, VH: RecyclerView.ViewHolder {
        val part = add(Items(vhClass, items))
        init?.invoke(part)
        return part
    }

    internal fun <R: AdapterPart> add(part: R): R {
        parts.add(part)
        return part
    }
}