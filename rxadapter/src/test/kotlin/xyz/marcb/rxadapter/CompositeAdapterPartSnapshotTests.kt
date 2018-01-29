package xyz.marcb.rxadapter

import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import kotlin.test.expect

internal class CompositeAdapterPartSnapshotTests {

    @Mock private lateinit var partA: AdapterPartSnapshot
    @Mock private lateinit var partB: AdapterPartSnapshot
    @Mock private lateinit var partC: AdapterPartSnapshot

    @Mock private lateinit var viewHolder: HeaderViewHolder

    private lateinit var snapshot: CompositeAdapterPartSnapshot

    @Before fun setUp() {
        MockitoAnnotations.initMocks(this)

        Mockito.`when`(partA.itemIds).thenReturn(listOf("A"))
        Mockito.`when`(partA.itemCount).thenReturn(1)

        Mockito.`when`(partB.itemIds).thenReturn(listOf("B", "C"))
        Mockito.`when`(partB.itemCount).thenReturn(2)

        Mockito.`when`(partC.itemIds).thenReturn(listOf("D"))
        Mockito.`when`(partC.itemCount).thenReturn(1)

        snapshot = CompositeAdapterPartSnapshot(parts = listOf(partA, partB, partC))
    }

    @Test fun itemIdsAreOrderedAggregationOfPartItemIds() {
        expect(listOf("A", "B", "C", "D")) { snapshot.itemIds }
    }

    @Test fun itemCountIsSumOfParts() {
        expect(4) { snapshot.itemCount }
    }

    @Test fun viewHolderClassDelegatesToPartWithAdjustedIndex() {
        snapshot.viewHolderClass(0)
        Mockito.verify(partA).viewHolderClass(0)

        snapshot.viewHolderClass(1)
        Mockito.verify(partB).viewHolderClass(0)

        snapshot.viewHolderClass(2)
        Mockito.verify(partB).viewHolderClass(1)

        snapshot.viewHolderClass(3)
        Mockito.verify(partC).viewHolderClass(0)
    }

    @Test fun bindDelegatesToPartWithAdjustedIndex() {
        snapshot.bind(viewHolder, 0)
        Mockito.verify(partA).bind(viewHolder, 0)

        snapshot.bind(viewHolder, 1)
        Mockito.verify(partB).bind(viewHolder, 0)

        snapshot.bind(viewHolder, 2)
        Mockito.verify(partB).bind(viewHolder, 1)

        snapshot.bind(viewHolder, 3)
        Mockito.verify(partC).bind(viewHolder, 0)
    }
}