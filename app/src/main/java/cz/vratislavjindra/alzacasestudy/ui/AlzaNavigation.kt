package cz.vratislavjindra.alzacasestudy.ui

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController

/**
 * Destinations used in the [AlzaCaseStudyApp].
 */
object AlzaDestinations {

    const val CATEGORIES_ROUTE = "categories"
    const val PRODUCTS_ROUTE = "products"
}

/**
 * Models the navigation actions in the app.
 */
class AlzaNavigationActions(navController: NavHostController) {
    val navigateToCategories: () -> Unit = {
        navController.navigate(route = AlzaDestinations.CATEGORIES_ROUTE) {
            // Pop up to the start destination of the graph to avoid building up a large stack of
            // destinations  on the back stack as users select items.
            popUpTo(id = navController.graph.findStartDestination().id) { saveState = true }
            // Avoid multiple copies of the same destination when reselecting the same item.
            launchSingleTop = true
            // Restore state when reselecting a previously selected item.
            restoreState = true
        }
    }
    val navigateToProducts: (categoryId: Int) -> Unit = { categoryId ->
        navController.navigate(
            route = "${AlzaDestinations.PRODUCTS_ROUTE}/$categoryId"
        ) {
            popUpTo(id = navController.graph.findStartDestination().id) { saveState = true }
            launchSingleTop = true
            restoreState = true
        }
    }
}