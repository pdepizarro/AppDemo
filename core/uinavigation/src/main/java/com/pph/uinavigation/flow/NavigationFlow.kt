package com.pph.uinavigation.flow

import com.pph.uinavigation.ScreenComposable

interface NavigationFlow {
    fun start()
    fun screenComposables(): List<ScreenComposable>
}

operator fun NavigationFlow.plus(navFlow: NavigationFlow) =
    this.screenComposables() + navFlow.screenComposables()