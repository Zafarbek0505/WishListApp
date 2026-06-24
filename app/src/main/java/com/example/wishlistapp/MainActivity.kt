package com.example.wishlistapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.example.wishlistapp.ui.theme.WishListAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val systemDark = isSystemInDarkTheme()
            var darkMode by rememberSaveable { mutableStateOf(systemDark) }

            WishListAppTheme(darkTheme = darkMode) {
                Navigation(
                    isDarkMode = darkMode,
                    onToggleTheme = { darkMode = !darkMode }
                )
            }
        }
    }
}
