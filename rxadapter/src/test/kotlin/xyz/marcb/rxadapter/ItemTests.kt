package xyz.marcb.rxadapter

import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import kotlin.test.expect

class ItemTests {

    @Mock private lateinit var viewHolder: HeaderViewHolder
    private val snapshotObserver = TestObserver<AdapterPartSnapshot>()
    private lateinit var item: Item<Optional<String>, HeaderViewHolder>

    @Before fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    private fun createWith(observable: Observable<Optional<String>>) {
        item = Item(HeaderViewHolder::class.java, observable).apply {
            binder = { item -> bind(item.value) }
        }
        item.snapshots.subscribe(snapshotObserver)
    }

    @Test fun itemCountIsOneForNonNullValue() {
        createWith(Observable.just("A".asOptional()))
        expect(1) { snapshotObserver.values()[0].itemCount }
    }

    @Test fun itemCountIsOneForNullValue() {
        createWith(Observable.just(Optional.None))
        expect(1) { snapshotObserver.values()[0].itemCount }
    }

    @Test fun viewHolderClassForNonNullValue() {
        createWith(Observable.just("A".asOptional()))
        val snapshot = snapshotObserver.values()[0]
        expect(HeaderViewHolder::class.java) { snapshot.viewHolderClass(0) }
    }

    @Test fun viewHolderClassForNullValue() {
        createWith(Observable.just(Optional.None))
        val snapshot = snapshotObserver.values()[0]
        expect(HeaderViewHolder::class.java) { snapshot.viewHolderClass(0) }
    }

    @Test fun binderIsInvokedForNonNullValue() {
        createWith(Observable.just("A".asOptional()))
        val snapshot = snapshotObserver.values()[0]
        snapshot.bind(viewHolder, index = 0)
        Mockito.verify(viewHolder).bind("A")
    }

    @Test fun binderIsInvokedForNullValue() {
        createWith(Observable.just(Optional.None))
        val snapshot = snapshotObserver.values()[0]
        snapshot.bind(viewHolder, index = 0)
        Mockito.verify(viewHolder).bind(null)
    }
}
