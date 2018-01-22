package xyz.marcb.rxadapter.app

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.TextView
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

        adapter.registerViewHolder(HeaderViewHolder::class.java, R.layout.item_header)
        adapter.registerViewHolder(DateViewHolder::class.java, R.layout.item_header)

        //
        // Header Section
        //
        adapter.section {

            // Static item
            item(HeaderViewHolder::class.java) {
                binder = {
                    title.text = "Dates to Remember"
                }
            }

            // Nullable item
            optionalItem<String, HeaderViewHolder>(HeaderViewHolder::class.java, Observable.just(null)) {
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
            item(DateViewHolder::class.java) {
                binder = {
                    title.text = "TODAY"
                }
            }

            items(DateViewHolder::class.java, items) {
                id = { date -> "" + date.hashCode() }
                binder = { date ->
                    title.text = date.toLocaleString()
                    itemView.setOnClickListener({
                        val newItems = ArrayList<Date>(items.value)
                        newItems.remove(date)
                        items.onNext(newItems)
                    })
                }
            }
        }

        // Bind it
        listView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        listView.adapter = adapter
    }

    internal class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.title)
    }

    internal class DateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.title)
    }
}
