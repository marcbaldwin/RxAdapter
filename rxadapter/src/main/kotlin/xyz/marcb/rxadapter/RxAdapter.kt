package xyz.marcb.rxadapter

import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import rx.Subscription
import xyz.marcb.rxadapter.internal.AdapterPartSnapshotDelta
import xyz.marcb.rxadapter.internal.EmptySnapshot
import java.util.*

open class RxAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val sections = ArrayList<AdapterPart>()
    private val vhFactories = SparseArray<(ViewGroup) -> RecyclerView.ViewHolder>()
    private val vhOnRecycledHandlers = SparseArray<(RecyclerView.ViewHolder) -> Unit>()

    private var snapshot: AdapterPartSnapshot = EmptySnapshot()
    private var adapterCount = 0
    private var subscription: Subscription? = null

    inline fun section(init: Section.() -> Unit): Section {
        val section = Section()
        init.invoke(section)
        addSection(section)
        return section
    }

    @Suppress("UNCHECKED_CAST")
    fun <VH: RecyclerView.ViewHolder> registerViewHolder(vhClass: Class<VH>, factory: (ViewGroup) -> RecyclerView.ViewHolder, onRecycled: (VH.() -> Unit)? = null) {
        vhFactories.put(vhClass.hashCode(), factory)

        onRecycled?.run {
            vhOnRecycledHandlers.put(vhClass.hashCode()) { viewHolder ->
                this(viewHolder as VH)
            }
        }
    }

    fun <VH> registerViewHolder(vhClass: Class<VH>, @LayoutRes layout: Int, onRecycled: (VH.() -> Unit)? = null) where VH : RecyclerView.ViewHolder {
        registerViewHolder(vhClass,
                factory = { parent ->
                    val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
                    vhClass.getConstructor(View::class.java).newInstance(view)
                },
                onRecycled = onRecycled
        )
    }

    fun addSection(section: Section) = sections.add(section)

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

    override fun getItemId(position: Int): Long = snapshot.itemIds[position]

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        vhOnRecycledHandlers[holder.javaClass.hashCode()]?.invoke(holder)
        snapshot.bind(holder, position)
    }

    override fun getItemViewType(position: Int): Int = snapshot.viewHolderClass(position).hashCode()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val factory = vhFactories[viewType] ?: error("Missing factory for view holder")
        return factory(parent)
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        vhOnRecycledHandlers[holder.javaClass.hashCode()]?.invoke(holder)
    }

    private fun onAdapterCountChange() {
        when (adapterCount) {
            0 -> {
                subscription?.unsubscribe()
                subscription = null
            }
            else -> {
                if (subscription == null) {
                    subscription = sections.combine().subscribe { newSnapshot ->
                        val diff = DiffUtil.calculateDiff(AdapterPartSnapshotDelta(snapshot, newSnapshot))
                        snapshot = newSnapshot
                        diff.dispatchUpdatesTo(this)
                    }
                }
            }
        }
    }
}
