package xyz.marcb.rxadapter

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.disposables.CompositeDisposable

open class RxViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val context: Context
        get() = itemView.context

    open val subscriptions = CompositeDisposable()

    open fun onViewRecycled() = subscriptions.clear()

    open fun onViewAttachedToWindow() {
    }

    open fun onViewDetachedFromWindow() {
        subscriptions.clear()
    }
}
