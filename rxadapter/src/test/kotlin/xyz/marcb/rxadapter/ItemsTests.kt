package xyz.marcb.rxadapter

import android.system.Os.bind
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import kotlin.test.expect

internal class ItemsTests {

    @Mock private lateinit var viewHolder: HeaderViewHolder
    private val snapshotObserver = TestObserver<AdapterPartSnapshot>()
    private lateinit var items: Items<String, HeaderViewHolder>

    @Before fun setUp() {
        MockitoAnnotations.initMocks(this)
        items = Items(HeaderViewHolder::class.java, Observable.just(listOf("1", "2", "3"))).apply {
            binder = { item -> bind(item) }
        }
        items.snapshots.subscribe(snapshotObserver)
    }

    @Test fun itemCount() {
        val snapshot = snapshotObserver.values()[0]
        expect(3) { snapshot.itemCount }
    }

    @Test fun viewHolderClass() {
        val snapshot = snapshotObserver.values()[0]
        expect(HeaderViewHolder::class.java) { snapshot.viewHolderClass(0) }
        expect(HeaderViewHolder::class.java) { snapshot.viewHolderClass(1) }
        expect(HeaderViewHolder::class.java) { snapshot.viewHolderClass(2) }
    }

    @Test fun binderIsInvoked() {
        val snapshot = snapshotObserver.values()[0]

        snapshot.bind(viewHolder, index = 0)
        Mockito.verify(viewHolder).bind("1")

        snapshot.bind(viewHolder, index = 1)
        Mockito.verify(viewHolder).bind("2")

        snapshot.bind(viewHolder, index = 2)
        Mockito.verify(viewHolder).bind("3")
    }
}
