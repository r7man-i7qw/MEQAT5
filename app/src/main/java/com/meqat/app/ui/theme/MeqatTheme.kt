package com.meqat.app.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

object MeqatPalette {
    val Background = Color(0xFFFAF9F6)
    val Surface = Color(0xFFFFFFFF)
    val Primary = Color(0xFF8A9A5B)
    val PrimaryDark = Color(0xFF657442)
    val Gold = Color(0xFFC6A96B)
    val TextPrimary = Color(0xFF333333)
    val TextSecondary = Color(0xFF858585)
    val Divider = Color(0xFFEAE8E1)
    val SoftGreen = Color(0xFFF0F3E9)
}

private val MeqatLightColors = lightColorScheme(
    primary = MeqatPalette.Primary,
    onPrimary = Color.White,
    primaryContainer = MeqatPalette.SoftGreen,
    onPrimaryContainer = MeqatPalette.PrimaryDark,
    secondary = MeqatPalette.Gold,
    background = MeqatPalette.Background,
    onBackground = MeqatPalette.TextPrimary,
    surface = MeqatPalette.Surface,
    onSurface = MeqatPalette.TextPrimary,
    outline = MeqatPalette.Divider
)

private val MeqatShapes = Shapes(
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(18.dp),
    large = RoundedCornerShape(26.dp)
)

@Composable
fun MeqatTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = MeqatLightColors,
        typography = Typography(),
        shapes = MeqatShapes,
        content = content
    )
}
