package com.example.wishlistapp.ui.theme

sealed class Screen(val route: String) {
    object HomeScreen : Screen("home_screen")
    object AddEditScreen : Screen("add_edit_screen")
    object DetailScreen : Screen("detail_screen")
}
