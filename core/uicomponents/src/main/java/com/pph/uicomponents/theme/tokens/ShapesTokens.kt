package com.pph.uicomponents.theme.tokens

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

private object BorderRadiusTokens {
    const val BORDER_RADIUS_XL = Int.MAX_VALUE
    const val BORDER_RADIUS_L = 16
    const val BORDER_RADIUS_M= 8
    const val BORDER_RADIUS_S = 4
}

val DemoAppShapesImpl = Shapes(
    small = getRoundedCornerShape(BorderRadiusTokens.BORDER_RADIUS_S.dp),
    medium = getRoundedCornerShape(BorderRadiusTokens.BORDER_RADIUS_M.dp),
    large = getRoundedCornerShape(BorderRadiusTokens.BORDER_RADIUS_L.dp),
    extraLarge = getRoundedCornerShape(BorderRadiusTokens.BORDER_RADIUS_XL.dp)
)

private fun getRoundedCornerShape(all: Dp = 0.dp): RoundedCornerShape = RoundedCornerShape(
    topStart = all, topEnd = all, bottomStart = all, bottomEnd = all
)
