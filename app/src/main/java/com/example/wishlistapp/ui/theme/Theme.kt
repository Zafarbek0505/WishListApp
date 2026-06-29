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
    primary              = NeonBlue,
    onPrimary            = DeepNavy,
    primaryContainer     = SlateCard,
    onPrimaryContainer   = NeonBlue,
    secondary            = CyberCyan,
    onSecondary          = DeepNavy,
    secondaryContainer   = MidnightCard,
    onSecondaryContainer = CyberCyan,
    tertiary             = CyberCyan,
    tertiaryContainer    = Color(0xFF003D55),
    onTertiaryContainer  = CyberCyan,
    background           = DeepNavy,
    onBackground         = Color.White,
    surface              = SpaceBlue,
    onSurface            = Color.White,
    surfaceVariant       = MidnightCard,
    onSurfaceVariant     = SteelGray,
    outline              = SlateCard,
    outlineVariant       = DimBlue,
    error                = TechRed,
    onError              = Color.White,
    errorContainer       = Color(0xFF4A0010),
    onErrorContainer     = TechRed
)

private val LightColorScheme = lightColorScheme(
    primary              = ElectricBlue,
    onPrimary            = Color.White,
    primaryContainer     = PowderBlue,
    onPrimaryContainer   = NavyInk,
    secondary            = Color(0xFF3A5080),
    onSecondary          = Color.White,
    secondaryContainer   = IceBlue,
    onSecondaryContainer = NavyInk,
    tertiary             = CyanMuted,
    tertiaryContainer    = Color(0xFFCCF0FF),
    onTertiaryContainer  = NavyInk,
    background           = CloudWhite,
    onBackground         = NavyInk,
    surface              = Color.White,
    onSurface            = NavyInk,
    surfaceVariant       = IceBlue,
    onSurfaceVariant     = SteelGray,
    outline              = Color(0xFFB8C8E8),
    outlineVariant       = PowderBlue,
    error                = TechRed,
    onError              = Color.White,
    errorContainer       = Color(0xFFFFD9DF),
    onErrorContainer     = Color(0xFF7A0020)
)

@Composable
fun WishListAppTheme(
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
        else      -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography  = Typography,
        content     = content
    )
}
