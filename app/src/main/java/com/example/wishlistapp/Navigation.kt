package com.example.wishlistapp

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.wishlistapp.ui.theme.Screen

private const val WishIdArg = "wishId"
private const val NewWishId = 0L

@Composable
fun Navigation(
    isDarkMode: Boolean,
    onToggleTheme: () -> Unit,
    viewModel: WishViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.HomeScreen.route
    ) {
        composable(Screen.HomeScreen.route) {
            HomeView(
                wishes = viewModel.wishes,
                isDarkMode = isDarkMode,
                onToggleTheme = onToggleTheme,
                onAddWish = {
                    navController.navigate("${Screen.AddEditScreen.route}/$NewWishId")
                },
                onEditWish = { wishId ->
                    navController.navigate("${Screen.AddEditScreen.route}/$wishId")
                },
                onDeleteWish = viewModel::deleteWish
            )
        }

        composable(
            route = "${Screen.AddEditScreen.route}/{$WishIdArg}",
            arguments = listOf(
                navArgument(WishIdArg) {
                    type = NavType.LongType
                    defaultValue = NewWishId
                }
            )
        ) { backStackEntry ->
            val wishId = backStackEntry.arguments?.getLong(WishIdArg) ?: NewWishId
            val wish = if (wishId == NewWishId) null else viewModel.getWish(wishId)

            AddEditWishView(
                wish = wish,
                isDarkMode = isDarkMode,
                onToggleTheme = onToggleTheme,
                onBack = { navController.popBackStack() },
                onSave = { title, description, priority ->
                    viewModel.saveWish(
                        id = wish?.id,
                        title = title,
                        description = description,
                        priority = priority
                    )
                    navController.popBackStack()
                }
            )
        }
    }
}
