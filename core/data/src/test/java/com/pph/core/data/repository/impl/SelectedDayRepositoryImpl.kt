package com.pph.core.data.repository.impl

import com.pph.data.model.dao.SelectedDayDao
import com.pph.data.model.entity.SelectedDayEntity
import com.pph.data.repository.impl.SelectedDayRepositoryImpl
import com.pph.domain.repository.SelectedDayRepository
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

class SelectedDayRepositoryImplTest {

    @MockK(relaxUnitFun = true)
    lateinit var dao: SelectedDayDao

    private lateinit var repository: SelectedDayRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        repository = SelectedDayRepositoryImpl(dao)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `saveSelectedDay should call dao with SelectedDayEntity with id zero and given epoch`() = runTest {
        // Given
        val epochDay = 12345L
        coEvery { dao.saveSelectedDay(any()) } returns Unit

        // When
        repository.saveSelectedDay(epochDay)

        // Then
        coVerify(exactly = 1) {
            dao.saveSelectedDay(
                SelectedDayEntity(
                    id = 0,
                    selectedDayEpoch = epochDay
                )
            )
        }
    }

    @Test
    fun `getSelectedDay should return value from dao`() = runTest {
        // Given
        val storedEpochDay = 67890L
        coEvery { dao.getSelectedDay() } returns storedEpochDay

        // When
        val result = repository.getSelectedDay()

        // Then
        assertEquals(storedEpochDay, result)
        coVerify(exactly = 1) { dao.getSelectedDay() }
    }

    @Test
    fun `getSelectedDay should return null when dao returns null`() = runTest {
        // Given
        coEvery { dao.getSelectedDay() } returns null

        // When
        val result = repository.getSelectedDay()

        // Then
        assertNull(result)
        coVerify(exactly = 1) { dao.getSelectedDay() }
    }
}
