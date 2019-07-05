package xyz.marcb.rxadapter.internal

import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import rx.Observable
import rx.observers.TestSubscriber
import xyz.marcb.rxadapter.AdapterPart
import xyz.marcb.rxadapter.AdapterPartSnapshot
import xyz.marcb.rxadapter.combine
import kotlin.test.expect

class CombineTests {

    @Mock private lateinit var adapterPartA: AdapterPart
    @Mock private lateinit var adapterPartB: AdapterPart

    @Mock private lateinit var adapterPartSnapshotA: AdapterPartSnapshot
    @Mock private lateinit var adapterPartSnapshotB: AdapterPartSnapshot

    private lateinit var snapshotSubscriber: TestSubscriber<AdapterPartSnapshot>

    @Before fun setUp() {
        MockitoAnnotations.initMocks(this)

        Mockito.`when`(adapterPartA.snapshots).thenReturn(Observable.just(adapterPartSnapshotA))
        Mockito.`when`(adapterPartB.snapshots).thenReturn(Observable.just(adapterPartSnapshotB))
        Mockito.`when`(adapterPartSnapshotA.itemIds).thenReturn(listOf(1))
        Mockito.`when`(adapterPartSnapshotB.itemIds).thenReturn(listOf(2, 3))
        Mockito.`when`(adapterPartSnapshotA.itemCount).thenReturn(1)
        Mockito.`when`(adapterPartSnapshotB.itemCount).thenReturn(2)

        snapshotSubscriber = TestSubscriber()
    }

    @Test fun includesSnapshotsWithNoVisibilityObservable() {
        listOf(adapterPartA, adapterPartB).combine().subscribe(snapshotSubscriber)

        expect(3) { snapshotSubscriber.onNextEvents[0].itemCount }
    }

    @Test fun includesVisibleSnapshots() {
        Mockito.`when`(adapterPartB.visible).thenReturn(Observable.just(true))
        listOf(adapterPartA, adapterPartB).combine().subscribe(snapshotSubscriber)

        expect(3) { snapshotSubscriber.onNextEvents[0].itemCount }
    }

    @Test fun ignoresHiddenSnapshots() {
        Mockito.`when`(adapterPartB.visible).thenReturn(Observable.just(false))
        listOf(adapterPartA, adapterPartB).combine().subscribe(snapshotSubscriber)

        expect(1) { snapshotSubscriber.onNextEvents[0].itemCount }
    }

    @Test fun emitsNewSnapshotWhenVisibilityChanges() {
        Mockito.`when`(adapterPartB.visible).thenReturn(Observable.just(false, true))
        listOf(adapterPartA, adapterPartB).combine().subscribe(snapshotSubscriber)

        expect(1) { snapshotSubscriber.onNextEvents[0].itemCount }
        expect(3) { snapshotSubscriber.onNextEvents[1].itemCount }
    }
}