package xyz.marcb.rxadapter

import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import kotlin.test.expect

class OptionalItemTests {

    @Mock private lateinit var viewHolder: HeaderViewHolder
    private val snapshotObserver = TestObserver<AdapterPartSnapshot>()
    private lateinit var item: OptionalItem<String, HeaderViewHolder>

    @Before fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    private fun createWith(observable: Observable<Optional<String>>) {
        item = OptionalItem(HeaderViewHolder::class.java, observable).apply {
            binder = { item -> bind(item) }
        }
        item.snapshots.subscribe(snapshotObserver)
    }

    @Test fun itemCountIsOneIfPresent() {
        createWith(Observable.just("A".asOptional()))
        expect(1) { snapshotObserver.values()[0].itemCount }
    }

    @Test fun itemCountIsZeroIfNotPresent() {
        createWith(Observable.just(Optional.None))
        expect(0) { snapshotObserver.values()[0].itemCount }
    }

    @Test fun viewHolderClass() {
        createWith(Observable.just("A".asOptional()))
        val snapshot = snapshotObserver.values()[0]
        expect(HeaderViewHolder::class.java) { snapshot.viewHolderClass(0) }
    }

    @Test fun binderIsInvoked() {
        createWith(Observable.just("A".asOptional()))
        val snapshot = snapshotObserver.values()[0]
        snapshot.bind(viewHolder, index = 0)
        Mockito.verify(viewHolder).bind("A")
    }
}
