package xyz.marcb.rxadapter

import androidx.recyclerview.widget.RecyclerView
import rx.Observable

class Section: AdapterPart {

    override val snapshots: Observable<AdapterPartSnapshot> get() = parts.combine()

    override var visible: Observable<Boolean>? = null

    private val parts = ArrayList<AdapterPart>()

    inline fun <VH> item(vhClass: Class<VH>, init: StaticItem<VH>.() -> Unit): StaticItem<VH>
            where VH: RecyclerView.ViewHolder {
        val part = add(StaticItem(vhClass))
        init.invoke(part)
        return part
    }

    inline fun <I, VH> item(vhClass: Class<VH>, item: Observable<I>, init: Item<I, VH>.() -> Unit): Item<I, VH>
            where I: Any?, VH: RecyclerView.ViewHolder {
        val part = add(Item(vhClass, item))
        init.invoke(part)
        return part
    }

    inline fun <I, VH> optionalItem(vhClass: Class<VH>, item: Observable<I?>, init: OptionalItem<I, VH>.() -> Unit): OptionalItem<I, VH>
            where I: Any, VH: RecyclerView.ViewHolder {
        val part = add(OptionalItem(vhClass, item))
        init.invoke(part)
        return part
    }

    inline fun <I, VH> items(vhClass: Class<VH>, items: List<I>, init: Items<I, VH>.() -> Unit): Items<I, VH>
            where I: Any, VH: RecyclerView.ViewHolder = items(vhClass, Observable.just(items), init)

    inline fun <I, VH> items(vhClass: Class<VH>, items: Observable<List<I>>, init: Items<I, VH>.() -> Unit): Items<I, VH>
            where I: Any, VH: RecyclerView.ViewHolder {
        val part = add(Items(vhClass, items))
        init.invoke(part)
        return part
    }

    fun <R: AdapterPart> add(part: R): R {
        parts.add(part)
        return part
    }
}