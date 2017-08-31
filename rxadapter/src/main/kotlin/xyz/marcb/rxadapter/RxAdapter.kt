package xyz.marcb.rxadapter

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import rx.Subscription

class RxAdapter {

    private val sections = ArrayList<AdapterPart>()
    private val vhFactories = HashMap<Int, (ViewGroup) -> RecyclerView.ViewHolder>()
    private val vhRecyclers = HashMap<Int, (RecyclerView.ViewHolder) -> Unit>()

    fun section(init: (Section.() -> Unit)? = null): Section {
        val section = Section()
        init?.invoke(section)
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

    fun create(): RecyclerView.Adapter<RecyclerView.ViewHolder> = Adapter(vhFactories, vhRecyclers, sections)
}

internal class Adapter(private val vhFactories: Map<Int, (ViewGroup) -> RecyclerView.ViewHolder>,
                       private val vhRecyclers: Map<Int, (RecyclerView.ViewHolder) -> Unit>,
                       private val parts: List<AdapterPart>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var snapshot: AdapterPartSnapshot = EmptySnapshot()
    private var adapterCount = 0
    private var subscription: Subscription? = null

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        adapterCount += 1
        onAdapterCountChange()
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        adapterCount -= 1
        onAdapterCountChange()
    }

    override fun getItemCount(): Int = snapshot.itemCount

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        vhRecyclers[holder.javaClass.hashCode()]?.invoke(holder)
        snapshot.bind(holder, position)
    }

    override fun getItemViewType(position: Int): Int = snapshot.viewHolderClass(position).hashCode()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val factory = vhFactories[viewType] ?: error("Missing factory for view holder")
        return factory(parent)
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        vhRecyclers[holder.javaClass.hashCode()]?.invoke(holder)
    }

    private fun onAdapterCountChange() {
        when (adapterCount) {
            0 -> {
                subscription?.unsubscribe()
                subscription = null
            }
            else -> {
                if (subscription == null) {
                    subscription = parts.combine().subscribe { newSnapshot ->
                        val diff = DiffUtil.calculateDiff(AdapterPartSnapshotDelta(snapshot, newSnapshot))
                        snapshot = newSnapshot
                        diff.dispatchUpdatesTo(this)
                    }
                }
            }
        }
    }
}
