package xyz.marcb.rxadapter.internal

import com.nhaarman.mockitokotlin2.whenever
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import xyz.marcb.rxadapter.AdapterPartSnapshot
import kotlin.test.expect

internal class AdapterPartSnapshotDeltaTests {

    @Mock private lateinit var old: AdapterPartSnapshot
    @Mock private lateinit var new: AdapterPartSnapshot

    @Before fun setUp() {
        MockitoAnnotations.initMocks(this)

        whenever(old.itemIds).thenReturn(listOf(1, 2))
        whenever(old.itemCount).thenReturn(2)
        whenever(new.itemIds).thenReturn(listOf(2, 3, 4))
        whenever(new.itemCount).thenReturn(3)
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