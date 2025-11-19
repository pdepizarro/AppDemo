package com.pph.uicomponents.components

import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.pph.uicomponents.theme.DemoAppTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ErrorComponentTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun errorComponent_displaysMessage() {
        val message = "Algo ha fallado"

        composeRule.setContent {
            DemoAppTheme {
                ErrorComponent(
                    message = message,
                    onRetryClick = {}
                )
            }
        }

        composeRule.onNodeWithText(message).assertIsDisplayed()
    }

    @Test
    fun errorComponent_showsWarningIcon() {
        composeRule.setContent {
            DemoAppTheme {
                ErrorComponent(
                    message = "msg",
                    onRetryClick = {}
                )
            }
        }

        composeRule
            .onNodeWithTag("errorIcon")
            .assertIsDisplayed()
    }

    @Test
    fun errorComponent_retryButtonIsClickable() {
        var clicked = false
        val retryText = "Reintentar"

        composeRule.setContent {
            DemoAppTheme {
                ErrorComponent(
                    message = "msg",
                    onRetryClick = { clicked = true }
                )
            }
        }

        composeRule
            .onNodeWithText(retryText)
            .assertIsDisplayed()
            .assertHasClickAction()
            .performClick()

        assert(clicked)
    }
}