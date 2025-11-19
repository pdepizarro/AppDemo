package com.pph.uicomponents.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalContext
import com.pph.uicomponents.theme.tokens.*

private val DarkColorScheme = darkColorScheme(
    primary = Blue80,
    secondary = BlueGrey80,
    tertiary = Yellow80,
    background = BackgroundDark,
    surface = CardDark
)

// Provide defaults using the IMPLEMENTATIONS
private val LocalDemoAppDimens = staticCompositionLocalOf { DemoAppDimensImpl }
private val LocalDemoAppShapes = staticCompositionLocalOf { DemoAppShapesImpl }
private val LocalDemoAppTypography = staticCompositionLocalOf { DemoAppTypographyImpl }

@Composable
fun DemoAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context)
            else dynamicLightColorScheme(context)
        }

        else -> DarkColorScheme
    }

    CompositionLocalProvider(
        LocalDemoAppDimens provides DemoAppDimensImpl,
        LocalDemoAppShapes provides DemoAppShapesImpl,
        LocalDemoAppTypography provides DemoAppTypographyImpl
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            content = content
        )
    }
}

object DemoAppTheme {
    val dimens: DemoAppDimens
        @Composable get() = LocalDemoAppDimens.current

    val shapes: Shapes
        @Composable get() = LocalDemoAppShapes.current

    val typography: DemoAppTypography
        @Composable get() = LocalDemoAppTypography.current
}
