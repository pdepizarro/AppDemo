package com.pph.demoapp.ui.navigation.flow

import androidx.navigation.NavHostController
import com.example.workinprogress.WorkInProgressScreenComposable
import com.pph.flow.NavigationFlow
import com.pph.uinavigation.ScreenComposable
import com.pph.uinavigation.navigateWith

class MainNavigationFlow(
    private val navController: NavHostController
) : NavigationFlow {
    override fun start() {
        navController.navigateWith<WorkInProgressScreenComposable>(clearStack = true)
    }

    override fun screenComposables(): List<ScreenComposable> = listOf(
        WorkInProgressScreenComposable()
    )

}