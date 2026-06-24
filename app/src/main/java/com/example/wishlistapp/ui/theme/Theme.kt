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
    primary = Azure95,
    secondary = Mint90,
    tertiary = Coral90,
    background = Ink10,
    surface = Graphite20,
    surfaceVariant = Color(0xFF303747),
    primaryContainer = Color(0xFF153A75),
    secondaryContainer = Color(0xFF164A39),
    tertiaryContainer = Color(0xFF703024),
    onPrimaryContainer = Color(0xFFD7E4FF),
    onSecondaryContainer = Color(0xFFD5F2E3),
    onTertiaryContainer = Color(0xFFFFDAD2)
)

private val LightColorScheme = lightColorScheme(
    primary = Azure40,
    secondary = Mint40,
    tertiary = Coral40,
    background = Mist98,
    surface = Color.White,
    surfaceVariant = Mist92,
    primaryContainer = Azure95,
    secondaryContainer = Mint90,
    tertiaryContainer = Coral90,
    onPrimaryContainer = Color(0xFF0B2754),
    onSecondaryContainer = Color(0xFF063725),
    onTertiaryContainer = Color(0xFF4F170D)
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
