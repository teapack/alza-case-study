package cz.vratislavjindra.alzacasestudy

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import cz.vratislavjindra.alzacasestudy.feature_products.presentation.product_categories.CategoriesScreen
import cz.vratislavjindra.alzacasestudy.feature_products.presentation.product_detail.ProductDetailScreen
import cz.vratislavjindra.alzacasestudy.feature_products.presentation.products.ProductsScreen

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.CategoriesScreen.route) {
        composable(route = Screen.CategoriesScreen.route) {
            CategoriesScreen(navController = navController)
        }
        composable(
            route = "${Screen.ProductsScreen.route}/{${Screen.ProductsScreen.argument1}}/{${
                Screen.ProductsScreen.argument2
            }}",
            arguments = listOf(
                navArgument(name = Screen.ProductsScreen.argument1!!) { type = NavType.IntType },
                navArgument(name = Screen.ProductsScreen.argument2!!) { type = NavType.StringType }
            )
        ) {
            ProductsScreen(
                navController = navController,
                categoryId = it.arguments?.getInt(Screen.ProductsScreen.argument1),
                categoryName = it.arguments?.getString(Screen.ProductsScreen.argument2)
            )
        }
        composable(
            route = "${Screen.ProductDetailScreen.route}/{${
                Screen.ProductDetailScreen.argument1
            }}/{${Screen.ProductDetailScreen.argument2}}",
            arguments = listOf(
                navArgument(name = Screen.ProductDetailScreen.argument1!!) {
                    type = NavType.IntType
                },
                navArgument(name = Screen.ProductDetailScreen.argument2!!) {
                    type = NavType.StringType
                }
            )
        ) {
            ProductDetailScreen(
                navController = navController,
                productId = it.arguments?.getInt(Screen.ProductDetailScreen.argument1),
                productName = it.arguments?.getString(Screen.ProductDetailScreen.argument2)
            )
        }
    }
}

sealed class Screen(
    val route: String,
    val argument1: String? = null,
    val argument2: String? = null
) {

    object CategoriesScreen : Screen(route = "categories")

    object ProductsScreen : Screen(
        route = "products",
        argument1 = "categoryId",
        argument2 = "categoryName"
    )

    object ProductDetailScreen : Screen(
        route = "product_detail",
        argument1 = "productId",
        argument2 = "productName"
    )
}