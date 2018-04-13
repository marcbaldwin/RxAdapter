package xyz.marcb.rxadapter

import android.support.v7.widget.RecyclerView
import android.view.View
import rx.subscriptions.CompositeSubscription

open class RxViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val subscriptions = CompositeSubscription()

    open fun recycle() = subscriptions.clear()
}
