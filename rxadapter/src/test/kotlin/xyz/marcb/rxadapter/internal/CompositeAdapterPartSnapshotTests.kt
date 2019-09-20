package xyz.marcb.rxadapter.internal

import com.nhaarman.mockitokotlin2.whenever
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import xyz.marcb.rxadapter.AdapterPartSnapshot
import xyz.marcb.rxadapter.HeaderViewHolder
import kotlin.test.expect

internal class CompositeAdapterPartSnapshotTests {

    @Mock private lateinit var partA: AdapterPartSnapshot
    @Mock private lateinit var partB: AdapterPartSnapshot
    @Mock private lateinit var partC: AdapterPartSnapshot

    @Mock private lateinit var viewHolder: HeaderViewHolder

    private lateinit var snapshot: CompositeAdapterPartSnapshot

    @Before fun setUp() {
        MockitoAnnotations.initMocks(this)

        whenever(partA.itemIds).thenReturn(listOf(1))
        whenever(partA.itemCount).thenReturn(1)

        whenever(partB.itemIds).thenReturn(listOf(2, 3))
        whenever(partB.itemCount).thenReturn(2)

        whenever(partC.itemIds).thenReturn(listOf(4))
        whenever(partC.itemCount).thenReturn(1)

        snapshot = CompositeAdapterPartSnapshot(parts = listOf(partA, partB, partC))
    }

    @Test fun itemIdsAreOrderedAggregationOfPartItemIds() {
        expect(listOf<Long>(1, 2, 3, 4)) { snapshot.itemIds }
    }

    @Test fun itemCountIsSumOfParts() {
        expect(4) { snapshot.itemCount }
    }

    @Test fun viewHolderClassDelegatesToPartWithAdjustedIndex() {
        snapshot.viewHolderClass(0)
        verify(partA).viewHolderClass(0)

        snapshot.viewHolderClass(1)
        verify(partB).viewHolderClass(0)

        snapshot.viewHolderClass(2)
        verify(partB).viewHolderClass(1)

        snapshot.viewHolderClass(3)
        verify(partC).viewHolderClass(0)
    }

    @Test fun bindDelegatesToPartWithAdjustedIndex() {
        snapshot.bind(viewHolder, 0)
        verify(partA).bind(viewHolder, 0)

        snapshot.bind(viewHolder, 1)
        verify(partB).bind(viewHolder, 0)

        snapshot.bind(viewHolder, 2)
        verify(partB).bind(viewHolder, 1)

        snapshot.bind(viewHolder, 3)
        verify(partC).bind(viewHolder, 0)
    }
}