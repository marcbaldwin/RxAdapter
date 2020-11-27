package xyz.marcb.rxadapter

import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.disposables.Disposable
import xyz.marcb.rxadapter.internal.AdapterPartSnapshotDelta
import xyz.marcb.rxadapter.internal.EmptySnapshot

open class RxAdapter : RecyclerView.Adapter<RxViewHolder>() {

    private val sections = ArrayList<AdapterPart>()
    private val vhFactories = SparseArray<(ViewGroup) -> RxViewHolder>()

    private var snapshot: AdapterPartSnapshot = EmptySnapshot
    private var subscription: Disposable? = null

    inline fun section(init: Section.() -> Unit): Section {
        val section = Section()
        init.invoke(section)
        addSection(section)
        return section
    }

    fun <VH : RecyclerView.ViewHolder> registerViewHolder(
        vhClass: Class<VH>,
        factory: (ViewGroup) -> RxViewHolder
    ) {
        vhFactories.put(vhClass.hashCode(), factory)
    }

    fun <VH> registerViewHolder(
        vhClass: Class<VH>,
        @LayoutRes layout: Int
    ) where VH : RxViewHolder {
        registerViewHolder(
            vhClass,
            factory = { parent ->
                val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
                vhClass.getConstructor(View::class.java).newInstance(view)
            }
        )
    }

    fun addSection(section: Section) = sections.add(section)

    override fun getItemCount(): Int = snapshot.itemCount

    override fun getItemId(position: Int): Long = snapshot.itemIds[position]

    override fun getItemViewType(position: Int): Int {
        return snapshot.viewHolderClass(position).hashCode()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RxViewHolder {
        val factory = vhFactories[viewType] ?: error("Missing factory for view holder")
        return factory(parent)
    }

    override fun onBindViewHolder(holder: RxViewHolder, position: Int) {
        snapshot.bind(holder, position)
    }

    override fun onViewAttachedToWindow(holder: RxViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.onViewAttachedToWindow()
    }

    override fun onViewDetachedFromWindow(holder: RxViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.onViewDetachedFromWindow()
    }

    override fun onViewRecycled(holder: RxViewHolder) {
        super.onViewRecycled(holder)
        holder.onViewRecycled()
    }

    fun start() {
        subscription = sections.combine().subscribe(::onNewSnapshot)
    }

    fun stop() {
        subscription?.dispose()
        subscription = null
    }

    private fun onNewSnapshot(newSnapshot: AdapterPartSnapshot) {
        val diff = DiffUtil.calculateDiff(AdapterPartSnapshotDelta(snapshot, newSnapshot))
        snapshot = newSnapshot
        diff.dispatchUpdatesTo(this)
    }
}
