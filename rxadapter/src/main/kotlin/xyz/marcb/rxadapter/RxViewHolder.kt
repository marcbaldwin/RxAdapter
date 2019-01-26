package xyz.marcb.rxadapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import rx.subscriptions.CompositeSubscription

open class RxViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    open val subscriptions = CompositeSubscription()

    open fun recycle() = subscriptions.clear()
}
