package xyz.marcb.rxadapter.app

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import xyz.marcb.rxadapter.RxAdapter
import xyz.marcb.rxadapter.RxViewHolder
import java.util.Collections.emptyList
import java.util.Date
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    data class Optional<T>(val value: T?)

    private lateinit var listView: RecyclerView

    private val items = BehaviorSubject.createDefault<List<Date>>(emptyList())
    private val adapter = RxAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listView = findViewById(R.id.dateslist)

        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        val fab = findViewById<View>(R.id.fab) as FloatingActionButton
        fab.setOnClickListener {
            val newItems = ArrayList(this.items.value!!)
            newItems.add(Date())
            this.items.onNext(newItems)
        }

        adapter.setHasStableIds(true)

        adapter.registerViewHolder(HeaderViewHolder::class.java, R.layout.item_header)
        adapter.registerViewHolder(DateViewHolder::class.java, R.layout.item_header)

        //
        // Header Section
        //
        adapter.section {

            // Static item
            item(HeaderViewHolder::class.java, id = 1) {
                binder = {
                    title.text = "Dates to Remember"
                }
            }

            // Nullable item
            optionalItem(
                HeaderViewHolder::class.java,
                Observable.just(Optional("Hi")),
                unwrap = { it.value },
                id = 2
            ) {
                binder = { text ->
                    title.text = text
                }
            }

            // Nullable item
            optionalItem(
                HeaderViewHolder::class.java,
                Observable.just(Optional<String>(null)),
                unwrap = { it.value },
                id = 3
            ) {
                binder = { text ->
                    title.text = text
                }
            }
        }

        //
        // Dates Section
        //
        adapter.section {

            // Observable items
            item(DateViewHolder::class.java, id = 4) {
                binder = {
                    title.text = "TODAY"
                }
            }

            items(DateViewHolder::class.java, items) {
                id = { date -> date.time }
                binder = { date ->
                    title.text = date.toLocaleString()
                }
                onClick = { date ->
                    val newItems = ArrayList(items.value!!)
                    newItems.remove(date)
                    items.onNext(newItems)
                }
            }
        }

        // Bind it
        listView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        listView.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        adapter.start()
    }

    override fun onPause() {
        super.onPause()
        adapter.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        listView.adapter = null
    }

    internal class HeaderViewHolder(itemView: View) : RxViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.title)
    }

    internal class DateViewHolder(itemView: View) : RxViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.title)
    }
}
