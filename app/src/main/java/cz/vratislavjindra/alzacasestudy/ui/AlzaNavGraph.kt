package cz.vratislavjindra.alzacasestudy.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import cz.vratislavjindra.alzacasestudy.feature_products.presentation.product_categories.CategoriesScreen
import cz.vratislavjindra.alzacasestudy.feature_products.presentation.product_detail.ProductDetailScreen
import cz.vratislavjindra.alzacasestudy.feature_products.presentation.products.ProductsScreen
import cz.vratislavjindra.alzacasestudy.ui.home.HomeRoute
import cz.vratislavjindra.alzacasestudy.ui.util.WindowInfo

@Composable
fun AlzaNavGraph(
    windowInfo: WindowInfo,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = AlzaDestinations.HOME_ROUTE
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(AlzaDestinations.HOME_ROUTE) { HomeRoute(windowInfo = windowInfo) }
//        composable(AlzaDestinations.PRODUCT_DETAIL_ROUTE) {
//            InterestsRoute(
//                interestsViewModel = interestsViewModel,
//                isExpandedScreen = isExpandedScreen,
//                openDrawer = openDrawer
//            )
//        }
    }
//    NavHost(navController = navController, startDestination = Screen.CategoriesScreen.route) {
//        composable(route = Screen.CategoriesScreen.route) {
//            CategoriesScreen(navController = navController)
//        }
//        composable(
//            route = "${Screen.ProductsScreen.route}/{${Screen.ProductsScreen.navArgument}}",
//            arguments = listOf(
//                navArgument(name = Screen.ProductsScreen.navArgument!!) { type = NavType.IntType }
//            )
//        ) {
//            ProductsScreen(
//                navController = navController,
//                categoryId = it.arguments?.getInt(Screen.ProductsScreen.navArgument)
//            )
//        }
//        composable(
//            route = "${Screen.ProductDetailScreen.route}/{${
//                Screen.ProductDetailScreen.navArgument
//            }}",
//            arguments = listOf(
//                navArgument(name = Screen.ProductDetailScreen.navArgument!!) {
//                    type = NavType.IntType
//                }
//            )
//        ) {
//            ProductDetailScreen(
//                navController = navController,
//                productId = it.arguments?.getInt(Screen.ProductDetailScreen.navArgument)
//            )
//        }
//    }
}

sealed class Screen(
    val route: String,
    val navArgument: String? = null
) {

    object CategoriesScreen : Screen(route = "categories")

    object ProductsScreen : Screen(
        route = "products",
        navArgument = "categoryId"
    )

    object ProductDetailScreen : Screen(
        route = "product_detail",
        navArgument = "productId"
    )
}