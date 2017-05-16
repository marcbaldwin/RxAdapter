package xyz.marcb.rxadapter

import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import rx.observers.TestSubscriber
import kotlin.test.expect

internal class ItemTests {

    lateinit var item: Item<HeaderViewHolder>
    @Mock lateinit var viewHolder: HeaderViewHolder
    val snapshotSubscriber = TestSubscriber<AdapterPartSnapshot>()

    @Before fun setUp() {
        MockitoAnnotations.initMocks(this)
        item = Item(HeaderViewHolder::class.java).apply {
            binder = { vh ->
                vh.bind("Test")
            }
        }
    }

    fun subscribeTakeFirst(): AdapterPartSnapshot {
        item.snapshots.subscribe(snapshotSubscriber)
        return snapshotSubscriber.onNextEvents[0]
    }

    @Test fun itemCountIsAlwaysOne() {
        val snapshot = subscribeTakeFirst()
        expect(1) { snapshot.itemCount }
    }

    @Test fun viewHolderClass() {
        val snapshot = subscribeTakeFirst()
        expect(HeaderViewHolder::class.java) { snapshot.viewHolderClass(0) }
    }

    @Test fun binderIsInvoked() {
        val snapshot = subscribeTakeFirst()
        snapshot.bind(viewHolder, index = 0)
        Mockito.verify(viewHolder).bind("Test")
    }
}