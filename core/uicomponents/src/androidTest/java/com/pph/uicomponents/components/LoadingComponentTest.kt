package com.pph.uicomponents.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.pph.uicomponents.theme.DemoAppTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoadingComponentTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun loadingComponent_displaysMessage() {
        composeRule.setContent {
            DemoAppTheme {
                LoadingComponent()
            }
        }

        composeRule
            .onNodeWithText("Simulamos una carga...")
            .assertIsDisplayed()
    }

    @Test
    fun loadingComponent_displaysCircularProgressIndicator() {
        composeRule.setContent {
            DemoAppTheme {
                LoadingComponent()
            }
        }

        composeRule
            .onNodeWithTag("cpiTag")
            .assertIsDisplayed()
    }
}
