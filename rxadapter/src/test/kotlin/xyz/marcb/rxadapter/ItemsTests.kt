package xyz.marcb.rxadapter

import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import rx.Observable
import rx.observers.TestSubscriber
import kotlin.test.expect

internal class ItemsTests {

    lateinit var items: Items<String, HeaderViewHolder>
    @Mock lateinit var viewHolder: HeaderViewHolder
    val snapshotSubscriber = TestSubscriber<AdapterPartSnapshot>()

    @Before fun setUp() {
        MockitoAnnotations.initMocks(this)
        items = Items(HeaderViewHolder::class.java, Observable.just(listOf("1", "2", "3"))).apply {
            binder = { item, viewHolder ->
                viewHolder.bind(item)
            }
        }
        items.snapshots.subscribe(snapshotSubscriber)
    }

    @Test fun itemCount() {
        val snapshot = snapshotSubscriber.onNextEvents[0]
        expect(3) { snapshot.itemCount }
    }

    @Test fun viewHolderClass() {
        val snapshot = snapshotSubscriber.onNextEvents[0]
        expect(HeaderViewHolder::class.java) { snapshot.viewHolderClass(0) }
        expect(HeaderViewHolder::class.java) { snapshot.viewHolderClass(1) }
        expect(HeaderViewHolder::class.java) { snapshot.viewHolderClass(2) }
    }

    @Test fun binderIsInvoked() {
        val snapshot = snapshotSubscriber.onNextEvents[0]

        snapshot.bind(viewHolder, index = 0)
        Mockito.verify(viewHolder).bind("1")

        snapshot.bind(viewHolder, index = 1)
        Mockito.verify(viewHolder).bind("2")

        snapshot.bind(viewHolder, index = 2)
        Mockito.verify(viewHolder).bind("3")
    }
}