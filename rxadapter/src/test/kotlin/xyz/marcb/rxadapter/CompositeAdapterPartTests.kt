package xyz.marcb.rxadapter

import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import kotlin.test.expect

internal class CompositeAdapterPartTests {

    @Mock lateinit var partA: AdapterPartSnapshot
    @Mock lateinit var partB: AdapterPartSnapshot
    @Mock lateinit var partC: AdapterPartSnapshot

    @Mock lateinit var viewHolder: HeaderViewHolder

    lateinit var adapterPart: CompositeAdapterPart

    @Before fun setUp() {
        MockitoAnnotations.initMocks(this)
        adapterPart = CompositeAdapterPart(parts = listOf(partA, partB, partC))

        Mockito.`when`(partA.itemCount).thenReturn(1)
        Mockito.`when`(partB.itemCount).thenReturn(2)
        Mockito.`when`(partC.itemCount).thenReturn(1)
    }

    @Test fun itemCountIsSumOfParts() {
        expect(4) { adapterPart.itemCount }
    }

    @Test fun viewHolderClassDelegatesToPartWithAdjustedIndex() {
        adapterPart.viewHolderClass(0)
        Mockito.verify(partA).viewHolderClass(0)

        adapterPart.viewHolderClass(1)
        Mockito.verify(partB).viewHolderClass(0)

        adapterPart.viewHolderClass(2)
        Mockito.verify(partB).viewHolderClass(1)

        adapterPart.viewHolderClass(3)
        Mockito.verify(partC).viewHolderClass(0)
    }

    @Test fun bindDelegatesToPartWithAdjustedIndex() {
        adapterPart.bind(viewHolder, 0)
        Mockito.verify(partA).bind(viewHolder, 0)

        adapterPart.bind(viewHolder, 1)
        Mockito.verify(partB).bind(viewHolder, 0)

        adapterPart.bind(viewHolder, 2)
        Mockito.verify(partB).bind(viewHolder, 1)

        adapterPart.bind(viewHolder, 3)
        Mockito.verify(partC).bind(viewHolder, 0)
    }
}