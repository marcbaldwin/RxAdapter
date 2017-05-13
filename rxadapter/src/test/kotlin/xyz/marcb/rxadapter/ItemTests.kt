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
        item.snapshots.subscribe(snapshotSubscriber)
    }

    @Test fun itemCountIsAlwaysOne() {
        val snapshot = snapshotSubscriber.onNextEvents[0]
        expect(1) { snapshot.itemCount }
    }

    @Test fun viewHolderClass() {
        val snapshot = snapshotSubscriber.onNextEvents[0]
        expect(HeaderViewHolder::class.java) { snapshot.viewHolderClass(0) }
    }

    @Test fun binderIsInvoked() {
        val snapshot = snapshotSubscriber.onNextEvents[0]
        snapshot.bind(viewHolder, index = 0)
        Mockito.verify(viewHolder).bind("Test")
    }
}