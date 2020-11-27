package xyz.marcb.rxadapter

import androidx.recyclerview.widget.RecyclerView
import io.reactivex.Observable

class Section : AdapterPart {

    override val snapshots: Observable<AdapterPartSnapshot> get() = parts.combine()

    override var visible: Observable<Boolean>? = null

    private val parts = ArrayList<AdapterPart>()

    inline fun <VH> item(
        vhClass: Class<VH>,
        id: Long = RecyclerView.NO_ID,
        init: StaticItem<VH>.() -> Unit = {}
    ): StaticItem<VH> where VH : RecyclerView.ViewHolder =
        add(StaticItem(vhClass, id)).apply { init.invoke(this) }

    inline fun <I, VH> item(
        vhClass: Class<VH>,
        item: Observable<I>,
        id: Long = RecyclerView.NO_ID,
        init: Item<I, VH>.() -> Unit = {}
    ): Item<I, VH> where VH : RecyclerView.ViewHolder =
        add(Item(vhClass, item, id)).apply { init.invoke(this) }

    inline fun <O, I, VH> optionalItem(
        vhClass: Class<VH>,
        item: Observable<O>,
        noinline unwrap: (O) -> I?,
        id: Long = RecyclerView.NO_ID,
        init: OptionalItem<O, I, VH>.() -> Unit = {}
    ): OptionalItem<O, I, VH> where VH : RecyclerView.ViewHolder =
        add(OptionalItem(vhClass, item, unwrap, id)).apply { init.invoke(this) }

    inline fun <I, VH> items(
        vhClass: Class<VH>,
        items: List<I>,
        init: Items<I, VH>.() -> Unit = {}
    ): Items<I, VH> where VH : RecyclerView.ViewHolder =
        items(vhClass, Observable.just(items), init)

    inline fun <I, VH> items(
        vhClass: Class<VH>,
        items: Observable<List<I>>,
        init: Items<I, VH>.() -> Unit
    ): Items<I, VH> where VH : RecyclerView.ViewHolder =
        add(Items(vhClass, items)).apply { init.invoke(this) }

    fun <R : AdapterPart> add(part: R): R {
        parts.add(part)
        return part
    }
}
