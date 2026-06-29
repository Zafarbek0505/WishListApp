package com.example.wishlistapp.ui.theme

sealed class Screen(val route: String) {
    object HomeScreen : Screen("home_screen")
    object AddEditScreen : Screen("add_edit_screen")
    object DetailScreen : Screen("detail_screen")
    object CategoriesScreen : Screen("categories_screen")
    object RemindersScreen : Screen("reminders_screen")
    object ProfileScreen : Screen("profile_screen")
}
