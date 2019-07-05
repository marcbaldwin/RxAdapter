package xyz.marcb.rxadapter.internal

import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import xyz.marcb.rxadapter.AdapterPartSnapshot
import kotlin.test.expect

internal class AdapterPartSnapshotDeltaTests {

    @Mock private lateinit var old: AdapterPartSnapshot
    @Mock private lateinit var new: AdapterPartSnapshot

    @Before fun setUp() {
        MockitoAnnotations.initMocks(this)

        Mockito.`when`(old.itemIds).thenReturn(listOf(1, 2))
        Mockito.`when`(old.itemCount).thenReturn(2)
        Mockito.`when`(new.itemIds).thenReturn(listOf(2, 3, 4))
        Mockito.`when`(new.itemCount).thenReturn(3)
    }

    @Test fun itemSize() {
        val delta = AdapterPartSnapshotDelta(old, new)
        expect(2) { delta.oldListSize }
        expect(3) { delta.newListSize }
    }

    @Test fun identicalItems() {
        val delta = AdapterPartSnapshotDelta(old, new)
        expect(false) { delta.areItemsTheSame(0, 0) }
        expect(true) { delta.areItemsTheSame(1, 0) }
    }
}