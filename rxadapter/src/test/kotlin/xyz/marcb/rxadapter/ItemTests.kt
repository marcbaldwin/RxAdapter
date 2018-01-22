package xyz.marcb.rxadapter

import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import rx.Observable
import rx.observers.TestSubscriber
import kotlin.test.expect

class ItemTests {

    lateinit var item: Item<String?, HeaderViewHolder>
    @Mock lateinit var viewHolder: HeaderViewHolder
    private val snapshotSubscriber = TestSubscriber<AdapterPartSnapshot>()

    @Before fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    private fun createWith(observable: Observable<String?>) {
        item = Item(HeaderViewHolder::class.java, observable).apply {
            binder = { item -> bind(item) }
        }
        item.snapshots.subscribe(snapshotSubscriber)
    }

    @Test fun itemCountIsOneForNonNullValue() {
        createWith(Observable.just("A"))
        expect(1) { snapshotSubscriber.onNextEvents[0].itemCount }
    }

    @Test fun itemCountIsOneForNullValue() {
        createWith(Observable.just(null))
        expect(1) { snapshotSubscriber.onNextEvents[0].itemCount }
    }

    @Test fun viewHolderClassForNonNullValue() {
        createWith(Observable.just("A"))
        val snapshot = snapshotSubscriber.onNextEvents[0]
        expect(HeaderViewHolder::class.java) { snapshot.viewHolderClass(0) }
    }

    @Test fun viewHolderClassForNullValue() {
        createWith(Observable.just(null))
        val snapshot = snapshotSubscriber.onNextEvents[0]
        expect(HeaderViewHolder::class.java) { snapshot.viewHolderClass(0) }
    }

    @Test fun binderIsInvokedForNonNullValue() {
        createWith(Observable.just("A"))
        val snapshot = snapshotSubscriber.onNextEvents[0]
        snapshot.bind(viewHolder, index = 0)
        Mockito.verify(viewHolder).bind("A")
    }

    @Test fun binderIsInvokedForNullValue() {
        createWith(Observable.just(null))
        val snapshot = snapshotSubscriber.onNextEvents[0]
        snapshot.bind(viewHolder, index = 0)
        Mockito.verify(viewHolder).bind(null)
    }
}