package xyz.marcb.rxadapter

import android.support.v7.widget.RecyclerView
import android.view.View
import io.reactivex.disposables.CompositeDisposable

open class RxViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val subscriptions = CompositeDisposable()

    open fun recycle() = subscriptions.clear()
}
