package com.pph.core.domain.usecases

import com.pph.domain.repository.SelectedDayRepository
import com.pph.domain.usecases.SaveSelectedDayUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.fail

class SaveSelectedDayUseCaseTest {

    @MockK
    lateinit var repo: SelectedDayRepository

    private lateinit var useCase: SaveSelectedDayUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        useCase = SaveSelectedDayUseCase(repo)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `invoke should call repository with correct epochDay`() = runTest {
        // Given
        val epochDay = 12345L
        coEvery { repo.saveSelectedDay(epochDay) } returns Unit

        // When
        useCase(epochDay)

        // Then
        coVerify(exactly = 1) { repo.saveSelectedDay(epochDay) }
    }

    @Test
    fun `invoke should rethrow CancellationException`() = runTest {
        // Given
        val epochDay = 999L
        val cancellation = CancellationException("Cancelled!")

        coEvery { repo.saveSelectedDay(epochDay) } throws cancellation

        try {
            // When
            useCase(epochDay)
            fail("Expected CancellationException to be thrown")
        } catch (e: CancellationException) {
            // OK — expected
        }

        coVerify(exactly = 1) { repo.saveSelectedDay(epochDay) }
    }
}
