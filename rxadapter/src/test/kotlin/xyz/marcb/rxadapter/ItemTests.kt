package xyz.marcb.rxadapter

import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import kotlin.test.expect

class ItemTests {

    @Mock private lateinit var viewHolder: HeaderViewHolder
    private lateinit var item: Item<String, HeaderViewHolder>
    private lateinit var testObserver: TestObserver<AdapterPartSnapshot>

    @Before fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    private fun createWith(observable: Observable<String>) {
        item = Item(HeaderViewHolder::class.java, observable).apply {
            binder = { item -> bind(item) }
        }
        testObserver = item.snapshots.test()
    }

    @Test
    fun itemCountIsOneWhenHasValue() {
        createWith(Observable.just("A"))
        testObserver.assertValueCount(1)
        expect(1) { testObserver.values()[0].itemCount }
    }

    @Test fun viewHolderClassForNonNullValue() {
        createWith(Observable.just("A"))
        val snapshot = testObserver.values()[0]
        expect(HeaderViewHolder::class.java) { snapshot.viewHolderClass(0) }
    }

    @Test fun binderIsInvokedForNonNullValue() {
        createWith(Observable.just("A"))
        val snapshot = testObserver.values()[0]
        snapshot.bind(viewHolder, index = 0)
        verify(viewHolder).bind("A")
    }
}