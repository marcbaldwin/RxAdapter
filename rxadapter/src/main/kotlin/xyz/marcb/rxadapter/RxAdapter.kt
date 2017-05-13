package xyz.marcb.rxadapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import rx.Observable

class RxAdapter {

    private val sections = ArrayList<AdapterPart>()
    private val vhFactories = HashMap<Int, (ViewGroup) -> RecyclerView.ViewHolder>()

    fun addSection(): Section {
        val section = Section()
        sections.add(section)
        return section
    }

    fun <VH: RecyclerView.ViewHolder> registerViewHolder(vhClass: Class<VH>, factory: (ViewGroup) -> RecyclerView.ViewHolder) {
        vhFactories[vhClass.hashCode()] = factory
    }

    fun create(): RecyclerView.Adapter<RecyclerView.ViewHolder> {
        return Adapter(vhFactories, sections)
    }
}

internal class Adapter(
        val vhFactories: Map<Int, (ViewGroup) -> RecyclerView.ViewHolder>,
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
        return vhFactories[viewType]!!(parent)
    }
}
