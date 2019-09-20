package xyz.marcb.rxadapter

import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import kotlin.test.expect

internal class ItemsTests {

    @Mock private lateinit var viewHolder: HeaderViewHolder
    private lateinit var testObserver: TestObserver<AdapterPartSnapshot>
    private lateinit var items: Items<String, HeaderViewHolder>

    @Before fun setUp() {
        MockitoAnnotations.initMocks(this)
        items = Items(HeaderViewHolder::class.java, Observable.just(listOf("1", "2", "3"))).apply {
            binder = { item -> bind(item) }
        }
        testObserver = items.snapshots.test()
    }

    @Test fun itemCount() {
        expect(3) { testObserver.values()[0].itemCount }
    }

    @Test fun viewHolderClass() {
        val snapshot = testObserver.values()[0]
        expect(HeaderViewHolder::class.java) { snapshot.viewHolderClass(0) }
        expect(HeaderViewHolder::class.java) { snapshot.viewHolderClass(1) }
        expect(HeaderViewHolder::class.java) { snapshot.viewHolderClass(2) }
    }

    @Test fun binderIsInvoked() {
        val snapshot = testObserver.values()[0]

        snapshot.bind(viewHolder, index = 0)
        verify(viewHolder).bind("1")

        snapshot.bind(viewHolder, index = 1)
        verify(viewHolder).bind("2")

        snapshot.bind(viewHolder, index = 2)
        verify(viewHolder).bind("3")
    }
}