package com.pph.uinavigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.Navigator
import androidx.navigation.navOptions
import kotlin.reflect.KClass

interface ScreenComposable {
    @Composable
    fun Create()

    val screenName: String get() = getCoordinatorScreenName(this::class)

    val navAnimation: NavAnimation get() = NavAnimation.FADE

}

fun <T : ScreenComposable> getCoordinatorScreenName(kClass: KClass<T>): String =
    kClass.simpleName ?: "Unknown"

inline fun <reified T : ScreenComposable> getCoordinatorScreenName(): String =
    getCoordinatorScreenName(T::class)

enum class NavAnimation {
    SLIDE, FADE, NONE;
}

inline fun <reified T : ScreenComposable> NavHostController.navigateWith(
    clearStack: Boolean = false, navigatorExtras: Navigator.Extras? = null
) {
    navigate(getCoordinatorScreenName<T>(), navOptions = navOptions {
        if (clearStack) {
            popUpTo(graph.startDestinationId) { inclusive = true }
            launchSingleTop = true
            restoreState = true
        }
    })
}

