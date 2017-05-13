package xyz.marcb.rxadapter

import android.support.v7.widget.RecyclerView
import rx.Observable

interface AdapterPart {

    val snapshots: Observable<AdapterPartSnapshot>
}

interface AdapterPartSnapshot {

    val itemCount: Int

    fun viewHolderClass(index: Int): Class<out RecyclerView.ViewHolder>
    fun bind(viewHolder: RecyclerView.ViewHolder, index: Int)
}