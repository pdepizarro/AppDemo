package com.pph.uicomponents.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalContext
import com.pph.uicomponents.theme.tokens.DemoAppDimens
import com.pph.uicomponents.theme.tokens.DemoAppDimensImpl
import com.pph.uicomponents.theme.tokens.DemoAppShapesImpl
import com.pph.uicomponents.theme.tokens.DemoAppTypography
import com.pph.uicomponents.theme.tokens.DemoAppTypographyImpl

private val DarkColorScheme = darkColorScheme(
    primary = Blue80, secondary = BlueGrey80, tertiary = Yellow80
)

private val LightColorScheme = lightColorScheme(
    primary = Blue40, secondary = BlueGrey40, tertiary = Yellow40
)

private val LocalDemoAppDimens = staticCompositionLocalOf { DemoAppDimens() }
private val LocalDemoAppShapes = staticCompositionLocalOf { Shapes() }
private val LocalDemoAppTypography = staticCompositionLocalOf { DemoAppTypography() }

@Composable
fun DemoAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    CompositionLocalProvider(
        LocalDemoAppDimens provides DemoAppDimensImpl,
        LocalDemoAppShapes provides DemoAppShapesImpl,
        LocalDemoAppTypography provides DemoAppTypographyImpl
    ) {
        MaterialTheme(
            colorScheme = colorScheme, content = content
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