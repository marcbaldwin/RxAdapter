package xyz.marcb.rxadapter.app

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import rx.Observable
import rx.subjects.BehaviorSubject
import xyz.marcb.rxadapter.RxAdapter
import java.util.*
import java.util.Collections.emptyList

class MainActivity : AppCompatActivity() {

    private lateinit var listView: RecyclerView

    private val items = BehaviorSubject.create<List<Date>>(emptyList<Date>())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listView = findViewById(R.id.dateslist)

        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        val fab = findViewById<View>(R.id.fab) as FloatingActionButton
        fab.setOnClickListener {
            val newItems = ArrayList<Date>(this.items.value)
            newItems.add(Date())
            this.items.onNext(newItems)
        }

        val adapter = RxAdapter()
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
            optionalItem<String, HeaderViewHolder>(HeaderViewHolder::class.java, Observable.just(null), id = 2) {
                binder = { _->
                    title.text = "Nulls are allowed"
                }
            }
        }

        //
        // Dates Section
        //
        adapter.section {

            // Observable items
            item(DateViewHolder::class.java, id = 3) {
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
                    val newItems = ArrayList<Date>(items.value)
                    newItems.remove(date)
                    items.onNext(newItems)
                }
            }
        }

        // Bind it
        listView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        listView.adapter = adapter
    }

    internal class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.title)
    }

    internal class DateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.title)
    }
}
