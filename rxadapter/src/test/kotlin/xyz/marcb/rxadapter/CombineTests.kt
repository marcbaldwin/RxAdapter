package xyz.marcb.rxadapter

import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import io.reactivex.subscribers.TestSubscriber
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import kotlin.test.expect

class CombineTests {

    @Mock private lateinit var adapterPartA: AdapterPart
    @Mock private lateinit var adapterPartB: AdapterPart

    @Mock private lateinit var adapterPartSnapshotA: AdapterPartSnapshot
    @Mock private lateinit var adapterPartSnapshotB: AdapterPartSnapshot

    private lateinit var snapshotObserver: TestObserver<AdapterPartSnapshot>

    @Before fun setUp() {
        MockitoAnnotations.initMocks(this)

        Mockito.`when`(adapterPartA.snapshots).thenReturn(Observable.just(adapterPartSnapshotA))
        Mockito.`when`(adapterPartB.snapshots).thenReturn(Observable.just(adapterPartSnapshotB))
        Mockito.`when`(adapterPartSnapshotA.itemIds).thenReturn(listOf("A"))
        Mockito.`when`(adapterPartSnapshotB.itemIds).thenReturn(listOf("B", "C"))
        Mockito.`when`(adapterPartSnapshotA.itemCount).thenReturn(1)
        Mockito.`when`(adapterPartSnapshotB.itemCount).thenReturn(2)

        snapshotObserver = TestObserver()
    }

    @Test fun includesSnapshotsWithNoVisibilityObservable() {
        listOf(adapterPartA, adapterPartB).combine().subscribe(snapshotObserver)

        expect(3) { snapshotObserver.values()[0].itemCount }
    }

    @Test fun includesVisibleSnapshots() {
        Mockito.`when`(adapterPartB.visible).thenReturn(Observable.just(true))
        listOf(adapterPartA, adapterPartB).combine().subscribe(snapshotObserver)

        expect(3) { snapshotObserver.values()[0].itemCount }
    }

    @Test fun ignoresHiddenSnapshots() {
        Mockito.`when`(adapterPartB.visible).thenReturn(Observable.just(false))
        listOf(adapterPartA, adapterPartB).combine().subscribe(snapshotObserver)

        expect(1) { snapshotObserver.values()[0].itemCount }
    }

    @Test fun emitsNewSnapshotWhenVisibilityChanges() {
        Mockito.`when`(adapterPartB.visible).thenReturn(Observable.just(false, true))
        listOf(adapterPartA, adapterPartB).combine().subscribe(snapshotObserver)

        expect(1) { snapshotObserver.values()[0].itemCount }
        expect(3) { snapshotObserver.values()[1].itemCount }
    }
}
