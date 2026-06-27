package com.asbdanja.tomatoguard.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// ── Brand Colors (Updated to match Splash texture) ───────────
val TomatoRed       = Color(0xFFE05252)
val TomatoRedDark   = Color(0xFFB83A3A)
val ForestGreen     = Color(0xFF0F2417)
val LeafGreen       = Color(0xFF5DAA70)
val LeafGreenLight  = Color(0xFF7DC48A)
val MintGreen       = Color(0xFF7DC48A)
val BackgroundLight = Color(0xFFF5F5F0)
val SurfaceLight    = Color(0xFFFFFFFF)
val SurfaceDark     = Color(0xFF1A3022)
val BackgroundDark  = Color(0xFF0F2417)

// Text/Overlay Helper (matches Splash specs)
val White38 = Color(0x61FFFFFF)

private val LightColorScheme = lightColorScheme(
    primary          = LeafGreen,
    onPrimary        = Color.White,
    primaryContainer = Color(0xFF263D2C),
    secondary        = TomatoRed,
    onSecondary      = Color.White,
    background       = BackgroundLight,
    surface          = SurfaceLight,
    onBackground     = Color(0xFF1A1A1A),
    onSurface        = Color(0xFF1A1A1A),
)

private val DarkColorScheme = darkColorScheme(
    primary          = LeafGreen,
    onPrimary        = Color.White,
    primaryContainer = Color(0xFF263D2C),
    secondary        = TomatoRed,
    onSecondary      = Color.White,
    background       = BackgroundDark,
    surface          = SurfaceDark,
    onBackground     = Color.White,
    onSurface        = Color.White,
    surfaceVariant   = Color(0xFF263D2C),
)

@Composable
fun TomatoGuardTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {

    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            // Set Status Bar to the new deep green texture
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography  = Typography, // Ensure Typography is defined in your Typography.kt
        content     = content
    )
}