package xyz.marcb.rxadapter.internal

import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import xyz.marcb.rxadapter.AdapterPart
import xyz.marcb.rxadapter.AdapterPartSnapshot
import xyz.marcb.rxadapter.combine
import kotlin.test.expect

class CombineTests {

    @Mock private lateinit var adapterPartA: AdapterPart
    @Mock private lateinit var adapterPartB: AdapterPart

    @Mock private lateinit var adapterPartSnapshotA: AdapterPartSnapshot
    @Mock private lateinit var adapterPartSnapshotB: AdapterPartSnapshot

    @Before fun setUp() {
        MockitoAnnotations.initMocks(this)

        whenever(adapterPartA.snapshots).thenReturn(Observable.just(adapterPartSnapshotA))
        whenever(adapterPartB.snapshots).thenReturn(Observable.just(adapterPartSnapshotB))
        whenever(adapterPartSnapshotA.itemIds).thenReturn(listOf(1))
        whenever(adapterPartSnapshotB.itemIds).thenReturn(listOf(2, 3))
        whenever(adapterPartSnapshotA.itemCount).thenReturn(1)
        whenever(adapterPartSnapshotB.itemCount).thenReturn(2)
    }

    @Test fun includesSnapshotsWithNoVisibilityObservable() {
        val testObserver = listOf(adapterPartA, adapterPartB).combine().test()
        expect(3) { testObserver.values()[0].itemCount }
    }

    @Test fun includesVisibleSnapshots() {
        whenever(adapterPartB.visible).thenReturn(Observable.just(true))
        val testObserver = listOf(adapterPartA, adapterPartB).combine().test()

        expect(3) { testObserver.values()[0].itemCount }
    }

    @Test fun ignoresHiddenSnapshots() {
        whenever(adapterPartB.visible).thenReturn(Observable.just(false))
        val testObserver = listOf(adapterPartA, adapterPartB).combine().test()

        expect(1) { testObserver.values()[0].itemCount }
    }

    @Test fun emitsNewSnapshotWhenVisibilityChanges() {
        whenever(adapterPartB.visible).thenReturn(Observable.just(false, true))
        val testObserver = listOf(adapterPartA, adapterPartB).combine().test()

        expect(1) { testObserver.values()[0].itemCount }
        expect(3) { testObserver.values()[1].itemCount }
    }
}