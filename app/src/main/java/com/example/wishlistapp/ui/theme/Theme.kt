package com.example.wishlistapp.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Sage90,
    secondary = Gold90,
    tertiary = Burgundy40,
    background = Ink10,
    surface = Graphite20,
    surfaceVariant = Color(0xFF332F27),
    primaryContainer = Color(0xFF0A4A34),
    secondaryContainer = Color(0xFF59421D),
    tertiaryContainer = Color(0xFF6E1823),
    onPrimaryContainer = Sage90,
    onSecondaryContainer = Gold90,
    onTertiaryContainer = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = Forest30,
    secondary = Forest45,
    tertiary = Gold45,
    background = Cream98,
    surface = Color(0xFFFFFCF7),
    surfaceVariant = Linen94,
    primaryContainer = Sage90,
    secondaryContainer = Gold90,
    tertiaryContainer = Color(0xFFF3E0DF),
    onPrimaryContainer = Color(0xFF073525),
    onSecondaryContainer = Color(0xFF4A3517),
    onTertiaryContainer = Burgundy40
)

@Composable
fun WishListAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
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

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
