package xyz.marcb.rxadapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import rx.Observable

class RxAdapter {

    private val sections = ArrayList<AdapterPart>()
    private val vhFactories = HashMap<Int, (ViewGroup) -> RecyclerView.ViewHolder>()
    private val vhRecyclers = HashMap<Int, (RecyclerView.ViewHolder) -> Unit>()

    fun addSection(): Section {
        val section = Section()
        sections.add(section)
        return section
    }

    @Suppress("UNCHECKED_CAST")
    fun <VH: RecyclerView.ViewHolder> registerViewHolder(vhClass: Class<VH>, factory: (ViewGroup) -> RecyclerView.ViewHolder, recycler: ((VH) -> Unit)? = null) {
        vhFactories[vhClass.hashCode()] = factory
        if (recycler != null) {
            vhRecyclers[vhClass.hashCode()] = { viewHolder ->
                recycler(viewHolder as VH)
            }
        }
    }

    fun create(): RecyclerView.Adapter<RecyclerView.ViewHolder> {
        return Adapter(vhFactories, vhRecyclers, sections)
    }
}

internal class Adapter(
        val vhFactories: Map<Int, (ViewGroup) -> RecyclerView.ViewHolder>,
        val vhRecylers: Map<Int, (RecyclerView.ViewHolder) -> Unit>,
        parts: ArrayList<AdapterPart>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var snapshot: AdapterPartSnapshot? = null

    init {
        Observable.combineLatest(parts.map { it.snapshots }) { snapshots ->
            CompositeAdapterPart(snapshots.map { it as AdapterPartSnapshot })
        }.subscribe { newSnapshot ->
            snapshot = newSnapshot
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return snapshot?.itemCount ?: 0
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        snapshot?.bind(holder, position)
    }

    override fun getItemViewType(position: Int): Int {
        return snapshot!!.viewHolderClass(position).hashCode()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val factory = vhFactories[viewType] ?: error("Missing factory for view holder")
        return factory(parent)
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        vhRecylers[holder.javaClass.hashCode()]?.invoke(holder)
    }
}
