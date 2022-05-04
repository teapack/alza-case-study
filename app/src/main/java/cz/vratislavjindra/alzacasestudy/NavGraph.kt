package cz.vratislavjindra.alzacasestudy

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import cz.vratislavjindra.alzacasestudy.feature_categories.presentation.categories_list.CategoriesListScreen

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.CategoriesScreen.route) {
        composable(route = Screen.CategoriesScreen.route) {
            CategoriesListScreen(navController = navController)
        }
        composable(
            route = "${Screen.ProductsScreen.route}/{categoryId}",
            arguments = listOf(
                navArgument(name = "categoryId") { type = NavType.IntType }
            )
        ) {

        }
        composable(
            route = "${Screen.ProductDetailScreen.route}/{productId}",
            arguments = listOf(
                navArgument(name = "categoryId") { type = NavType.IntType }
            )
        ) {

        }
    }
}

sealed class Screen(val route: String) {

    object CategoriesScreen : Screen(route = "categories")

    object ProductsScreen : Screen(route = "products")

    object ProductDetailScreen : Screen(route = "product_detail")
}