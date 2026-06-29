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
    viewModel: WishViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    fun navigateToTab(route: String) {
        navController.navigate(route) {
            popUpTo(Screen.HomeScreen.route) { saveState = true; inclusive = false }
            launchSingleTop = true
            restoreState = true
        }
    }

    NavHost(
        navController = navController,
        startDestination = Screen.HomeScreen.route
    ) {
        composable(Screen.HomeScreen.route) {
            HomeView(
                wishes = viewModel.wishes,
                onAddWish = { navController.navigate("${Screen.AddEditScreen.route}/$NewWishId") },
                onOpenWish = { wishId -> navController.navigate("${Screen.DetailScreen.route}/$wishId") },
                currentRoute = Screen.HomeScreen.route,
                onNavigate = ::navigateToTab
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
                onBack = { navController.popBackStack() },
                onSave = { title, description, priority, category, targetPrice, savedAmount, dueDate, reminder, imageUri ->
                    viewModel.saveWish(
                        id = wish?.id,
                        title = title,
                        description = description,
                        priority = priority,
                        category = category,
                        targetPrice = targetPrice,
                        savedAmount = savedAmount,
                        dueDate = dueDate,
                        reminder = reminder,
                        imageUri = imageUri
                    )
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = "${Screen.DetailScreen.route}/{$WishIdArg}",
            arguments = listOf(navArgument(WishIdArg) { type = NavType.LongType })
        ) { backStackEntry ->
            val wishId = backStackEntry.arguments?.getLong(WishIdArg) ?: NewWishId
            val wish = viewModel.getWish(wishId)

            if (wish == null) {
                navController.popBackStack()
            } else {
                DetailWishView(
                    wish = wish,
                    onBack = { navController.popBackStack() },
                    onEdit = { navController.navigate("${Screen.AddEditScreen.route}/${wish.id}") },
                    onDelete = {
                        viewModel.deleteWish(wish.id)
                        navController.popBackStack(Screen.HomeScreen.route, inclusive = false)
                    },
                    onMarkBought = { viewModel.markWishAsBought(wish.id) },
                    currentRoute = Screen.HomeScreen.route,
                    onNavigate = ::navigateToTab
                )
            }
        }

        composable(Screen.CategoriesScreen.route) {
            CategoriesView(
                wishes = viewModel.wishes,
                onOpenWish = { wishId -> navController.navigate("${Screen.DetailScreen.route}/$wishId") },
                currentRoute = Screen.CategoriesScreen.route,
                onNavigate = ::navigateToTab
            )
        }

        composable(Screen.RemindersScreen.route) {
            RemindersView(
                wishes = viewModel.wishes,
                onOpenWish = { wishId -> navController.navigate("${Screen.DetailScreen.route}/$wishId") },
                currentRoute = Screen.RemindersScreen.route,
                onNavigate = ::navigateToTab
            )
        }

        composable(Screen.ProfileScreen.route) {
            ProfileView(
                wishes = viewModel.wishes,
                currentRoute = Screen.ProfileScreen.route,
                onNavigate = ::navigateToTab
            )
        }
    }
}
