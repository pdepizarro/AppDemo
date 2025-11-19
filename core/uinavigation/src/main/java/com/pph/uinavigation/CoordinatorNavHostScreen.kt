package com.pph.uinavigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import kotlin.reflect.KClass

@Composable
fun <T: ScreenComposable> CoordinatorNavHostScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    startDestination: KClass<T>,
    screenComposableList: List<ScreenComposable>,
    navAnimation: NavAnimation? = null
){
    NavHost(
        navController = navHostController,
        startDestination = getCoordinatorScreenName(startDestination),
        modifier = modifier
    ){
        screenComposableList.forEach{ screenComposable ->
            this.composable(screenComposable.screenName,
                enterTransition = {
                    when(navAnimation ?: screenComposable.navAnimation){
                        NavAnimation.SLIDE -> slideIntoContainer(
                            AnimatedContentTransitionScope.SlideDirection.Start,
                            tween(500)
                        )
                        NavAnimation.NONE, NavAnimation.FADE -> fadeIn(tween(500))
                    }
                },
                exitTransition = {
                    when(navAnimation ?: screenComposable.navAnimation){
                        NavAnimation.SLIDE -> slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.Start,
                            tween(500)
                        )
                        NavAnimation.NONE, NavAnimation.FADE -> fadeOut(tween(500))
                    }
                }) { screenComposable.Create() }

        }
    }
}