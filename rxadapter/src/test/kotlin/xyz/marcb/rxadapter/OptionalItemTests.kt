package xyz.marcb.rxadapter

import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import kotlin.test.expect

class OptionalItemTests {

    private data class Optional(val string: String?)

    @Mock private lateinit var viewHolder: HeaderViewHolder
    private lateinit var item: OptionalItem<Optional, String, HeaderViewHolder>

    @Before fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    private fun createWith(observable: Observable<Optional>) {
        item = OptionalItem(HeaderViewHolder::class.java, observable, { it.string }).apply {
            binder = { item -> bind(item) }
        }
    }

    @Test fun itemCountIsOneIfPresent() {
        createWith(Observable.just(Optional("A")))
        val testObserver = item.snapshots.test()
        expect(1) { testObserver.values()[0].itemCount }
    }

    @Test fun itemCountIsZeroIfNotPresent() {
        createWith(Observable.just(Optional(null)))
        val testObserver = item.snapshots.test()
        expect(0) { testObserver.values()[0].itemCount }
    }

    @Test fun viewHolderClass() {
        createWith(Observable.just(Optional("A")))
        val testObserver = item.snapshots.test()
        val snapshot = testObserver.values()[0]
        expect(HeaderViewHolder::class.java) { snapshot.viewHolderClass(0) }
    }

    @Test fun binderIsInvoked() {
        createWith(Observable.just(Optional("A")))
        val testObserver = item.snapshots.test()
        val snapshot = testObserver.values()[0]
        snapshot.bind(viewHolder, index = 0)
        verify(viewHolder).bind("A")
    }
}