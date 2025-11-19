package com.pph.uicomponents.components

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
class ErrorDialogTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun errorDialog_displaysTitleAndMessage() {
        val message = "Ha ocurrido un error inesperado"

        composeRule.setContent {
            DemoAppTheme {
                ErrorDialog(
                    message = message,
                    onDismiss = {},
                    onRetryClick = null
                )
            }
        }

        composeRule
            .onNodeWithTag("errorDialogTitle")
            .assertIsDisplayed()

        composeRule
            .onNodeWithText(message)
            .assertIsDisplayed()
    }

    @Test
    fun errorDialog_closeButtonCallsOnDismiss() {
        var dismissed = false

        composeRule.setContent {
            DemoAppTheme {
                ErrorDialog(
                    message = "msg",
                    onDismiss = { dismissed = true },
                    onRetryClick = null
                )
            }
        }

        composeRule
            .onNodeWithTag("errorDialogCloseButton")
            .assertIsDisplayed()
            .performClick()

        assert(dismissed)
    }

    @Test
    fun errorDialog_showsRetryButtonWhenOnRetryProvided() {
        var retried = false

        composeRule.setContent {
            DemoAppTheme {
                ErrorDialog(
                    message = "msg",
                    onDismiss = {},
                    onRetryClick = { retried = true }
                )
            }
        }

        composeRule
            .onNodeWithTag("errorDialogRetryButton")
            .assertIsDisplayed()
            .performClick()

        assert(retried)
    }

    @Test
    fun errorDialog_doesNotShowRetryButtonWhenOnRetryIsNull() {
        composeRule.setContent {
            DemoAppTheme {
                ErrorDialog(
                    message = "msg",
                    onDismiss = {},
                    onRetryClick = null
                )
            }
        }

        composeRule
            .onNodeWithTag("errorDialogRetryButton")
            .assertDoesNotExist()
    }
}
