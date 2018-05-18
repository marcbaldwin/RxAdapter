package xyz.marcb.rxadapter

import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import kotlin.test.expect

internal class StaticItemTests {

    @Mock private lateinit var viewHolder: HeaderViewHolder
    private val snapshotObserver = TestObserver<AdapterPartSnapshot>()
    private lateinit var item: StaticItem<HeaderViewHolder>

    @Before fun setUp() {
        MockitoAnnotations.initMocks(this)
        item = StaticItem(HeaderViewHolder::class.java).apply {
            binder = { bind("Test") }
        }
    }

    private fun subscribeTakeFirst(): AdapterPartSnapshot {
        item.snapshots.subscribe(snapshotObserver)
        return snapshotObserver.values()[0]
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
