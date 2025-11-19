package com.pph.demoapp.ui.navigation.flow

import androidx.navigation.NavHostController
import com.pph.details.ForecastDetailScreenComposable
import com.pph.uinavigation.flow.NavigationFlow
import com.pph.forecast.ForecastScreenComposable
import com.pph.uinavigation.ScreenComposable
import com.pph.uinavigation.navigateWith

class MainNavigationFlow(
    private val navController: NavHostController
) : NavigationFlow {

    override fun start() {
        navController.navigateWith<ForecastScreenComposable>(clearStack = true)
    }

    override fun screenComposables(): List<ScreenComposable> = listOf(
        ForecastScreenComposable(onDayDetailNavigation = { navController.navigateWith<ForecastDetailScreenComposable>() }),
        ForecastDetailScreenComposable()
    )
}