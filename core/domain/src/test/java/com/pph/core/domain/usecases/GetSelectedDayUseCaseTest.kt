package com.pph.core.domain.usecases

import com.pph.domain.repository.SelectedDayRepository
import com.pph.domain.usecases.GetSelectedDayUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull

class GetSelectedDayUseCaseTest {

    @MockK
    lateinit var repo: SelectedDayRepository

    private lateinit var useCase: GetSelectedDayUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        useCase = GetSelectedDayUseCase(repo)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `invoke should return value from repository`() = runTest {
        // Given
        val storedEpochDay = 12345L
        coEvery { repo.getSelectedDay() } returns storedEpochDay

        // When
        val result = useCase()

        // Then
        assertEquals(storedEpochDay, result)
        coVerify(exactly = 1) { repo.getSelectedDay() }
    }

    @Test
    fun `invoke should return null when repository returns null`() = runTest {
        // Given
        coEvery { repo.getSelectedDay() } returns null

        // When
        val result = useCase()

        // Then
        assertNull(result)
        coVerify(exactly = 1) { repo.getSelectedDay() }
    }
}
