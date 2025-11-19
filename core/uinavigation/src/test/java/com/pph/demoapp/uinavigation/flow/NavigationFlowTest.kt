package com.pph.demoapp.uinavigation.flow

import com.pph.uinavigation.ScreenComposable
import com.pph.uinavigation.flow.NavigationFlow
import com.pph.uinavigation.flow.plus
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Before
import org.junit.Test

class NavigationFlowPlusOperatorTest {

    @MockK
    lateinit var flow1: NavigationFlow

    @MockK
    lateinit var flow2: NavigationFlow

    @MockK
    lateinit var screenA: ScreenComposable

    @MockK
    lateinit var screenB: ScreenComposable

    @MockK
    lateinit var screenC: ScreenComposable

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `plus operator should concatenate screen lists in correct order`() {
        // Given
        every { flow1.screenComposables() } returns listOf(screenA, screenB)
        every { flow2.screenComposables() } returns listOf(screenC)

        // When
        val result = flow1 + flow2

        // Then
        assertEquals(3, result.size)
        assertSame(screenA, result[0])
        assertSame(screenB, result[1])
        assertSame(screenC, result[2])
    }

    @Test
    fun `plus operator should return only second list when first is empty`() {
        // Given
        every { flow1.screenComposables() } returns emptyList()
        every { flow2.screenComposables() } returns listOf(screenA)

        // When
        val result = flow1 + flow2

        // Then
        assertEquals(1, result.size)
        assertSame(screenA, result[0])
    }

    @Test
    fun `plus operator should return only first list when second is empty`() {
        // Given
        every { flow1.screenComposables() } returns listOf(screenA)
        every { flow2.screenComposables() } returns emptyList()

        // When
        val result = flow1 + flow2

        // Then
        assertEquals(1, result.size)
        assertSame(screenA, result[0])
    }

    @Test
    fun `plus operator should return empty list when both lists are empty`() {
        // Given
        every { flow1.screenComposables() } returns emptyList()
        every { flow2.screenComposables() } returns emptyList()

        // When
        val result = flow1 + flow2

        // Then
        assertEquals(0, result.size)
    }
}
