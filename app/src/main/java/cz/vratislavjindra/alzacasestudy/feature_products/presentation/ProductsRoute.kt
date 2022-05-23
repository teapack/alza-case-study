package cz.vratislavjindra.alzacasestudy.feature_products.presentation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import cz.vratislavjindra.alzacasestudy.ui.components.snackbar.SnackbarData
import kotlinx.coroutines.flow.Flow

/**
 * Displays the Products route.
 *
 * @param viewModel ViewModel that handles the business logic of this screen
 * @param isExpandedScreen (state) whether the screen is expanded
 * @param onBack (event) request to navigate back
 */
@Composable
fun ProductsRoute(
    viewModel: ProductsViewModel = hiltViewModel(),
    isExpandedScreen: Boolean,
    onBack: () -> Unit
) {
    // UI state of the Products screen.
    val uiState by viewModel.uiState.collectAsState()
    ProductsRoute(
        uiState = uiState,
        isExpandedScreen = isExpandedScreen,
        snackbarFlow = viewModel.snackbarFlow,
//        onToggleFavorite = { viewModel.toggleFavourite(it) },
        onSelectProduct = { viewModel.selectProduct(productId = it) },
        onRefreshProducts = viewModel::refreshProducts,
        onRefreshProductDetail = viewModel::refreshProductDetail,
        onInteractWithProductsList = viewModel::interactedWithProductList,
        onBack = onBack,
        onProductActionClick = viewModel::interactedWithProductAction
    )
}

/**
 * Displays the Products route.
 *
 * This composable is not coupled to any specific state management.
 *
 * @param uiState (state) the data to show on the screen
 * @param isExpandedScreen (state) whether the screen is expanded
// * @param onToggleFavorite (event) toggles favorite for a product
 * @param onSelectProduct (event) indicate that a product was selected
 * @param onRefreshProducts (event) request a refresh of products
 * @param onRefreshProductDetail (event) request a refresh of product detail
 * @param onInteractWithProductsList (event) indicate that the products list was interacted with
 * @param onBack (event) request to navigate back
 */
@Composable
fun ProductsRoute(
    uiState: ProductsUiState,
    isExpandedScreen: Boolean,
    snackbarFlow: Flow<SnackbarData>,
//    onToggleFavorite: (String) -> Unit,
    onSelectProduct: (Int) -> Unit,
    onRefreshProducts: () -> Unit,
    onRefreshProductDetail: () -> Unit,
    onInteractWithProductsList: () -> Unit,
    onBack: () -> Unit,
    onProductActionClick: () -> Unit
) {
    // Construct the lazy grid state for the products grid outside of deciding which screen type to
    // show. This allows the associated state to survive beyond that decision, and therefore we get
    // to preserve the scroll throughout any changes to the content.
    val productsLazyGridState = rememberLazyGridState()
    val productsScreenType = getProductsScreenType(
        isExpandedScreen = isExpandedScreen,
        uiState = uiState
    )
    when (productsScreenType) {
        ProductsScreenType.ProductsListWithProductDetail -> {
            ProductsListWithProductDetailScreen(
                productsLazyGridState = productsLazyGridState,
                uiState = uiState,
                snackbarFlow = snackbarFlow,
                onRefreshProducts = onRefreshProducts,
                onSelectProduct = onSelectProduct,
                onProductActionClick = onProductActionClick,
//                onToggleFavorite = onToggleFavorite,
                onRefreshProductDetail = onRefreshProductDetail,
                onBack = onBack
            )
        }
        ProductsScreenType.ProductsList -> {
            ProductsListScreen(
                productsLazyGridState = productsLazyGridState,
                uiState = uiState,
                snackbarFlow = snackbarFlow,
                onRefresh = onRefreshProducts,
                onSelectProduct = onSelectProduct,
                onProductActionClick = onProductActionClick,
//                onToggleFavorite = onToggleFavorite,
                onBack = onBack
            )
        }
        ProductsScreenType.ProductDetail -> {
            // Guaranteed by above condition for products screen type.
            check(uiState is ProductsUiState.HasProducts)
            ProductDetailScreen(
                snackbarFlow = snackbarFlow,
                product = uiState.selectedProduct,
                loading = uiState.productDetailLoading,
                errorMessage = uiState.productDetailErrorMessage,
                onRefresh = onRefreshProductDetail,
                onProductActionClick = onProductActionClick,
//                isFavorite = uiState.favorites.contains(uiState.selectedProduct.id),
//                onToggleFavorite = { onToggleFavorite(uiState.selectedProduct.id) },
                onBack = onInteractWithProductsList
            )
            // If we are just showing the detail, have a back press switch to the list. This doesn't
            // take anything more than notifying that we "interacted with the list" since that is
            // what drives the display of the products list.
            BackHandler {
                onInteractWithProductsList()
            }
        }
    }
}

/**
 * A precise enumeration of which type of screen to display at the products route.
 *
 * There are 3 options:
 * - [ProductsListWithProductDetail], which displays both a list of products and a specific product.
 * - [ProductsList], which displays just the list of products.
 * - [ProductDetail], which displays just a specific product.
 */
private enum class ProductsScreenType {
    ProductsListWithProductDetail,
    ProductsList,
    ProductDetail
}

/**
 * Returns the current [ProductsScreenType] to display, based on whether or not the screen is
 * expanded and the [ProductsUiState].
 */
@Composable
private fun getProductsScreenType(
    isExpandedScreen: Boolean,
    uiState: ProductsUiState
): ProductsScreenType = when (isExpandedScreen) {
    false -> {
        when (uiState) {
            is ProductsUiState.HasProducts -> {
                if (uiState.selectedProduct != null) {
                    ProductsScreenType.ProductDetail
                } else {
                    ProductsScreenType.ProductsList
                }
            }
            is ProductsUiState.NoProducts -> ProductsScreenType.ProductsList
        }
    }
    true -> ProductsScreenType.ProductsListWithProductDetail
}