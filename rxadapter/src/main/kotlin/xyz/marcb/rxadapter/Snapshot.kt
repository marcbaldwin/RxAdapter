package xyz.marcb.rxadapter

import android.support.v7.widget.RecyclerView

internal class Snapshot<I, VH>(
        private val vhClass: Class<VH>,
        private val items: List<I>,
        override val itemIds: List<String>,
        private val binder: (VH.(I) -> Unit)?,
        private val onClick: (VH.(I) -> Unit)?
) : AdapterPartSnapshot where I : Any?, VH : RecyclerView.ViewHolder {

    override fun viewHolderClass(index: Int): Class<VH> = vhClass

    @Suppress("UNCHECKED_CAST")
    override fun bind(viewHolder: RecyclerView.ViewHolder, index: Int) {
        val typeSafeViewHolder = viewHolder as VH
        val item = items[index]
        binder?.invoke(typeSafeViewHolder, item)
        onClick?.let { onClick ->
            viewHolder.itemView.setOnClickListener {
                onClick.invoke(typeSafeViewHolder, item)
            }
        }
    }

    override fun underlyingObject(index: Int): Any? = items[index]
}