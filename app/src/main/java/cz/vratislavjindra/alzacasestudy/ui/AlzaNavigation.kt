package cz.vratislavjindra.alzacasestudy.ui

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController

/**
 * Destinations used in the app.
 */
object AlzaDestinations {

    const val HOME_ROUTE = "home"
    const val PRODUCT_DETAIL_ROUTE = "product_detail"
}

/**
 * Models the navigation actions in the app.
 */
class AlzaNavigationActions(navController: NavHostController) {
    val navigateToHome: () -> Unit = {
        navController.navigate(route = AlzaDestinations.HOME_ROUTE) {
            // Pop up to the start destination of the graph to avoid building up a large stack of
            // destinations  on the back stack as users select items.
            popUpTo(id = navController.graph.findStartDestination().id) { saveState = true }
            // Avoid multiple copies of the same destination when reselecting the same item.
            launchSingleTop = true
            // Restore state when reselecting a previously selected item.
            restoreState = true
        }
    }
    val navigateToProductDetail: (productId: Int) -> Unit = { productId ->
        navController.navigate(route = "${AlzaDestinations.PRODUCT_DETAIL_ROUTE}/$productId") {
            popUpTo(id = navController.graph.findStartDestination().id) { saveState = true }
            launchSingleTop = true
            restoreState = true
        }
    }
}