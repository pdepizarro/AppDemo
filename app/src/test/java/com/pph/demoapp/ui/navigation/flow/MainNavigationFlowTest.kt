package com.pph.demoapp.ui.navigation.flow

import androidx.navigation.NavHostController
import com.pph.details.ForecastDetailScreenComposable
import com.pph.forecast.ForecastScreenComposable
import com.pph.uinavigation.ScreenComposable
import com.pph.uinavigation.getCoordinatorScreenName
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue

class MainNavigationFlowTest {

    @MockK(relaxUnitFun = true)
    lateinit var navController: NavHostController

    private lateinit var navigationFlow: MainNavigationFlow

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        navigationFlow = MainNavigationFlow(navController)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `start should navigate to ForecastScreenComposable clearing back stack`() {
        every { navController.graph.startDestinationId } returns 1
        every { navController.navigate(any<String>(), any(), any()) } just Runs

        // When
        navigationFlow.start()

        // Then
        verify(exactly = 1) {
            navController.navigate(
                getCoordinatorScreenName<ForecastScreenComposable>(),
                any(),
                null
            )
        }
    }

    @Test
    fun `screenComposables should return two screens in correct order`() {
        // When
        val screens: List<ScreenComposable> = navigationFlow.screenComposables()

        // Then
        assertEquals(2, screens.size)
        assertTrue(screens[0] is ForecastScreenComposable)
        assertTrue(screens[1] is ForecastDetailScreenComposable)
    }
}
