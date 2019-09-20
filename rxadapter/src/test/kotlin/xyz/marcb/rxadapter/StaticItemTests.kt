package xyz.marcb.rxadapter

import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import kotlin.test.expect

internal class StaticItemTests {

    @Mock private lateinit var viewHolder: HeaderViewHolder
    private lateinit var testObserver: TestObserver<AdapterPartSnapshot>
    private lateinit var item: StaticItem<HeaderViewHolder>

    @Before fun setUp() {
        MockitoAnnotations.initMocks(this)
        item = StaticItem(HeaderViewHolder::class.java).apply {
            binder = { bind("Test") }
        }
        testObserver = item.snapshots.test()
    }

    @Test fun itemCountIsAlwaysOne() {
        expect(1) { testObserver.values()[0].itemCount }
    }

    @Test fun viewHolderClass() {
        expect(HeaderViewHolder::class.java) { testObserver.values()[0].viewHolderClass(0) }
    }

    @Test fun binderIsInvoked() {
        testObserver.values()[0].bind(viewHolder, index = 0)
        verify(viewHolder).bind("Test")
    }
}