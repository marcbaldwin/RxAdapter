package xyz.marcb.rxadapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.disposables.CompositeDisposable

open class RxViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    open val subscriptions = CompositeDisposable()

    open fun recycle() = subscriptions.clear()
}
