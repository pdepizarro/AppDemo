package com.pph.uinavigation

import androidx.navigation.NavHostController
import androidx.navigation.navOptions

fun NavHostController.navigateTopLevel(route: String) {
    navigate(route, navOptions {
        popUpTo(graph.startDestinationId) { saveState = true }
        launchSingleTop = true
        restoreState = true
    })
}
