package cz.vratislavjindra.alzacasestudy.ui

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import cz.vratislavjindra.alzacasestudy.feature_categories.presentation.categories_list.CategoriesRoute
import cz.vratislavjindra.alzacasestudy.feature_products.presentation.ProductsRoute

@Composable
fun AlzaNavGraph(
    widthSizeClass: WindowWidthSizeClass,
    navController: NavHostController = rememberNavController(),
    startDestination: String = AlzaDestinations.CATEGORIES_ROUTE,
    navigationActions: AlzaNavigationActions
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(route = AlzaDestinations.CATEGORIES_ROUTE) {
            CategoriesRoute(onCategoryClick = { navigationActions.navigateToProducts(it) })
        }
        composable(
            route = "${AlzaDestinations.PRODUCTS_ROUTE}/{category_id}",
            arguments = listOf(
                navArgument(name = "category_id") { type = NavType.IntType }
            )
        ) {
            ProductsRoute(
                isExpandedScreen = widthSizeClass == WindowWidthSizeClass.Expanded,
                onBack = { navController.navigateUp() }
            )
        }
    }
}