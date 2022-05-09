package cz.vratislavjindra.alzacasestudy.ui.home

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import cz.vratislavjindra.alzacasestudy.R
import cz.vratislavjindra.alzacasestudy.feature_products.presentation.product_categories.CategoriesScreen
import cz.vratislavjindra.alzacasestudy.feature_products.presentation.product_categories.CategoriesScreenContent
import cz.vratislavjindra.alzacasestudy.feature_products.presentation.product_categories.NoCategoriesScreen
import cz.vratislavjindra.alzacasestudy.feature_products.presentation.product_detail.NoProductDetailScreen
import cz.vratislavjindra.alzacasestudy.feature_products.presentation.product_detail.NoProductDetailScreenContent
import cz.vratislavjindra.alzacasestudy.feature_products.presentation.product_detail.ProductDetailScreen
import cz.vratislavjindra.alzacasestudy.feature_products.presentation.product_detail.ProductDetailScreenContent
import cz.vratislavjindra.alzacasestudy.feature_products.presentation.products.NoProductsScreen
import cz.vratislavjindra.alzacasestudy.feature_products.presentation.products.NoProductsScreenContent
import cz.vratislavjindra.alzacasestudy.feature_products.presentation.products.ProductsScreen
import cz.vratislavjindra.alzacasestudy.feature_products.presentation.products.ProductsScreenContent
import cz.vratislavjindra.alzacasestudy.ui.components.layout.ThreePane
import cz.vratislavjindra.alzacasestudy.ui.components.layout.TwoPane
import cz.vratislavjindra.alzacasestudy.ui.util.WindowInfo

@Composable
fun HomeRoute(
    viewModel: HomeViewModel = hiltViewModel(),
    windowInfo: WindowInfo
) {
    val uiState by viewModel.uiState.collectAsState()
    HomeRoute(
        uiState = uiState,
        windowInfo = windowInfo,
        onCategoryClick = { viewModel.selectCategory(it) },
        onProductClick = { viewModel.selectProduct(it) },
        onProductActionClick = { viewModel.interactedWithProductAction() },
        onInteractWithProductDetail = { viewModel.interactedWithProductDetail() },
        onInteractWithProductsList = { viewModel.interactedWithProductList() },
        onRefreshCategories = { viewModel.refreshCategories() },
        onRefreshProducts = { viewModel.loadProducts() },
        onRefreshProductDetail = { viewModel.loadProductDetail() }
    )
}

@Composable
fun HomeRoute(
    uiState: HomeUiState,
    windowInfo: WindowInfo,
    onCategoryClick: (Int) -> Unit,
    onProductClick: (Int) -> Unit,
    onProductActionClick: () -> Unit,
    onInteractWithProductDetail: () -> Unit,
    onInteractWithProductsList: () -> Unit,
    onRefreshCategories: () -> Unit,
    onRefreshProducts: () -> Unit,
    onRefreshProductDetail: () -> Unit
) {
    // Construct the lazy list states for the lists and the details outside of deciding which one to
    // show. This allows the associated state to survive beyond that decision, and therefore we get
    // to preserve the scroll throughout any changes to the content.
    val categoriesLazyGridState = rememberLazyGridState()
    val productsLazyGridStates = when (uiState) {
        is HomeUiState.HasCategories -> uiState.categories
        is HomeUiState.HasCategoriesNoProducts -> uiState.categories
        is HomeUiState.HasCategoriesHasProducts -> uiState.categories
        is HomeUiState.HasCategoriesHasProductsNoProductDetail -> uiState.categories
        is HomeUiState.HasCategoriesHasProductsHasProductDetail -> uiState.categories
        is HomeUiState.NoCategories -> emptyList()
    }.associate { category ->
        key(category.id) {
            category.id to rememberLazyGridState()
        }
    }
    val homeScreenType = getHomeScreenType(
        screenWidthSize = windowInfo.screenWidthSize,
        uiState = uiState
    )
    when (homeScreenType) {
        HomeScreenType.Categories -> if (uiState is HomeUiState.NoCategories) {
            NoCategoriesScreen(
                loading = uiState.categoriesLoading,
                errors = uiState.errors,
                onRefresh = onRefreshCategories
            )
        } else {
            // Has categories (guaranteed by above condition for home screen type).
            check(uiState is HomeUiState.HasCategories)
            CategoriesScreen(
                categoriesLazyGridState = categoriesLazyGridState,
                categories = uiState.categories,
                selectedCategoryId = null,
                loading = uiState.categoriesLoading,
                errors = uiState.errors,
                onRefresh = onRefreshCategories,
                onCategorySelected = onCategoryClick
            )
        }
        HomeScreenType.Products -> if (uiState is HomeUiState.HasCategoriesNoProducts) {
            NoProductsScreen(
                loading = uiState.productsLoading,
                errors = uiState.errors,
                onUpClick = onInteractWithProductsList,
                onRefresh = onRefreshProducts
            )
            // If we are just showing the products list, have a back press switch to the list. This
            // doesn't take anything more than notifying that we "interacted with the products list"
            // since that is what drives the display of the list.
            BackHandler {
                onInteractWithProductsList()
            }
        } else {
            // Has categories, has products (guaranteed by above condition for home screen type).
            check(uiState is HomeUiState.HasCategoriesHasProducts)
            ProductsScreen(
                productsLazyGridStates = productsLazyGridStates,
                products = uiState.products,
                selectedProductId = null,
                selectedCategoryId = uiState.selectedCategoryId,
                loading = uiState.productsLoading,
                errors = uiState.errors,
                onUpClick = onInteractWithProductsList,
                onRefresh = onRefreshProducts,
                onProductActionClick = onProductActionClick,
                onProductSelected = onProductClick
            )
            // If we are just showing the products list, have a back press switch to the list. This
            // doesn't take anything more than notifying that we "interacted with the products list"
            // since that is what drives the display of the list.
            BackHandler {
                onInteractWithProductsList()
            }
        }
        HomeScreenType.ProductDetail ->
            if (uiState is HomeUiState.HasCategoriesHasProductsNoProductDetail) {
                NoProductDetailScreen(
                    loading = uiState.productDetailLoading,
                    errors = uiState.errors,
                    onUpClick = onInteractWithProductDetail,
                    onRefresh = onRefreshProductDetail
                )
                BackHandler {
                    onInteractWithProductDetail()
                }
            } else {
                // Has categories, has products, has product detail (guaranteed by above condition
                // for home screen type).
                check(uiState is HomeUiState.HasCategoriesHasProductsHasProductDetail)
                ProductDetailScreen(
                    product = uiState.selectedProduct,
                    loading = uiState.productDetailLoading,
                    errors = uiState.errors,
                    onUpClick = onInteractWithProductDetail,
                    onRefresh = onRefreshProductDetail,
                    onProductActionClick = { /*TODO*/ }
                )
                BackHandler {
                    onInteractWithProductDetail()
                }
            }
        HomeScreenType.CategoriesWithProducts ->
            if (uiState is HomeUiState.HasCategoriesNoProducts) {
                TwoPane(
                    title = stringResource(id = R.string.app_name),
                    upNavigationAction = onInteractWithProductsList,
                    topAppBarActions = listOf(
                        /*TODO*/
                    ),
                    leftPaneContent = { modifier, paddingValues ->
                        CategoriesScreenContent(
                            modifier = modifier,
                            categoriesLazyGridState = categoriesLazyGridState,
                            paddingValues = paddingValues,
                            categories = uiState.categories,
                            selectedCategoryId = uiState.selectedCategoryId,
                            loading = uiState.categoriesLoading,
                            errors = uiState.errors,
                            onRefresh = onRefreshCategories,
                            onCategorySelected = onCategoryClick
                        )
                    },
                    rightPaneContent = { modifier, paddingValues ->
                        NoProductsScreenContent(
                            modifier = modifier,
                            paddingValues = paddingValues,
                            loading = uiState.productsLoading,
                            errors = uiState.errors,
                            onRefresh = onRefreshProducts
                        )
                    }
                )
                BackHandler {
                    onInteractWithProductsList()
                }
            } else {
                // Has categories, has products (guaranteed by above condition for home screen
                // type).
                check(uiState is HomeUiState.HasCategoriesHasProducts)
                TwoPane(
                    title = stringResource(id = R.string.app_name),
                    upNavigationAction = onInteractWithProductsList,
                    topAppBarActions = listOf(
                        /*TODO*/
                    ),
                    leftPaneContent = { modifier, paddingValues ->
                        CategoriesScreenContent(
                            modifier = modifier,
                            categoriesLazyGridState = categoriesLazyGridState,
                            paddingValues = paddingValues,
                            categories = uiState.categories,
                            selectedCategoryId = uiState.selectedCategoryId,
                            loading = uiState.categoriesLoading,
                            errors = uiState.errors,
                            onRefresh = onRefreshCategories,
                            onCategorySelected = onCategoryClick
                        )
                    },
                    rightPaneContent = { modifier, paddingValues ->
                        ProductsScreenContent(
                            modifier = modifier,
                            productsLazyGridStates = productsLazyGridStates,
                            paddingValues = paddingValues,
                            products = uiState.products,
                            selectedProductId = null,
                            selectedCategoryId = uiState.selectedCategoryId,
                            loading = uiState.productsLoading,
                            errors = uiState.errors,
                            onRefresh = onRefreshProducts,
                            onProductActionClick = onProductActionClick,
                            onProductSelected = onProductClick
                        )
                    }
                )
                BackHandler {
                    onInteractWithProductsList()
                }
            }
        HomeScreenType.ProductsWithProductDetail ->
            if (uiState is HomeUiState.HasCategoriesHasProductsNoProductDetail) {
                TwoPane(
                    title = stringResource(id = R.string.app_name),
                    upNavigationAction = onInteractWithProductDetail,
                    topAppBarActions = listOf(
                        /*TODO*/
                    ),
                    leftPaneContent = { modifier, paddingValues ->
                        ProductsScreenContent(
                            modifier = modifier,
                            productsLazyGridStates = productsLazyGridStates,
                            paddingValues = paddingValues,
                            products = uiState.products,
                            selectedProductId = uiState.selectedProductId,
                            selectedCategoryId = uiState.selectedCategoryId,
                            loading = uiState.productsLoading,
                            errors = uiState.errors,
                            onRefresh = onRefreshProducts,
                            onProductActionClick = onProductActionClick,
                            onProductSelected = onProductClick
                        )
                    },
                    rightPaneContent = { modifier, paddingValues ->
                        NoProductDetailScreenContent(
                            modifier = modifier,
                            paddingValues = paddingValues,
                            loading = uiState.productsLoading,
                            errors = uiState.errors,
                            onRefresh = onRefreshProducts
                        )
                    }
                )
                BackHandler {
                    onInteractWithProductDetail()
                }
            } else {
                // Has categories, has products, has product detail (guaranteed by above condition
                // for home screen type).
                check(uiState is HomeUiState.HasCategoriesHasProductsHasProductDetail)
                TwoPane(
                    title = stringResource(id = R.string.app_name),
                    upNavigationAction = onInteractWithProductDetail,
                    topAppBarActions = listOf(
                        /*TODO*/
                    ),
                    leftPaneContent = { modifier, paddingValues ->
                        ProductsScreenContent(
                            modifier = modifier,
                            productsLazyGridStates = productsLazyGridStates,
                            paddingValues = paddingValues,
                            products = uiState.products,
                            selectedProductId = uiState.selectedProductId,
                            selectedCategoryId = uiState.selectedCategoryId,
                            loading = uiState.productsLoading,
                            errors = uiState.errors,
                            onRefresh = onRefreshProducts,
                            onProductActionClick = onProductActionClick,
                            onProductSelected = onProductClick
                        )
                    },
                    rightPaneContent = { modifier, paddingValues ->
                        ProductDetailScreenContent(
                            modifier = modifier,
                            paddingValues = paddingValues,
                            product = uiState.selectedProduct,
                            loading = uiState.productsLoading,
                            errors = uiState.errors,
                            onRefresh = onRefreshProductDetail,
                            onProductActionClick = onProductActionClick
                        )
                    }
                )
                BackHandler {
                    onInteractWithProductDetail()
                }
            }
        HomeScreenType.CategoriesWithProductsWithProductDetail ->
            if (uiState is HomeUiState.HasCategoriesHasProductsNoProductDetail) {
                ThreePane(
                    title = stringResource(id = R.string.app_name),
                    upNavigationAction = onInteractWithProductDetail,
                    topAppBarActions = listOf(
                        /*TODO*/
                    ),
                    leftPaneContent = { modifier, paddingValues ->
                        CategoriesScreenContent(
                            modifier = modifier,
                            categoriesLazyGridState = categoriesLazyGridState,
                            paddingValues = paddingValues,
                            categories = uiState.categories,
                            selectedCategoryId = uiState.selectedCategoryId,
                            loading = uiState.categoriesLoading,
                            errors = uiState.errors,
                            onRefresh = onRefreshCategories,
                            onCategorySelected = onCategoryClick
                        )
                    },
                    middlePaneContent = { modifier, paddingValues ->
                        ProductsScreenContent(
                            modifier = modifier,
                            productsLazyGridStates = productsLazyGridStates,
                            paddingValues = paddingValues,
                            products = uiState.products,
                            selectedProductId = uiState.selectedProductId,
                            selectedCategoryId = uiState.selectedCategoryId,
                            loading = uiState.productsLoading,
                            errors = uiState.errors,
                            onRefresh = onRefreshProducts,
                            onProductActionClick = onProductActionClick,
                            onProductSelected = onProductClick
                        )
                    },
                    rightPaneContent = { modifier, paddingValues ->
                        NoProductDetailScreenContent(
                            modifier = modifier,
                            paddingValues = paddingValues,
                            loading = uiState.productsLoading,
                            errors = uiState.errors,
                            onRefresh = onRefreshProducts
                        )
                    }
                )
                BackHandler {
                    onInteractWithProductDetail()
                }
            } else {
                // Has categories, has products, has product detail (guaranteed by above condition
                // for home screen type).
                check(uiState is HomeUiState.HasCategoriesHasProductsHasProductDetail)
                ThreePane(
                    title = stringResource(id = R.string.app_name),
                    upNavigationAction = onInteractWithProductDetail,
                    topAppBarActions = listOf(
                        /*TODO*/
                    ),
                    leftPaneContent = { modifier, paddingValues ->
                        CategoriesScreenContent(
                            modifier = modifier,
                            categoriesLazyGridState = categoriesLazyGridState,
                            paddingValues = paddingValues,
                            categories = uiState.categories,
                            selectedCategoryId = uiState.selectedCategoryId,
                            loading = uiState.categoriesLoading,
                            errors = uiState.errors,
                            onRefresh = onRefreshCategories,
                            onCategorySelected = onCategoryClick
                        )
                    },
                    middlePaneContent = { modifier, paddingValues ->
                        ProductsScreenContent(
                            modifier = modifier,
                            productsLazyGridStates = productsLazyGridStates,
                            paddingValues = paddingValues,
                            products = uiState.products,
                            selectedProductId = uiState.selectedProductId,
                            selectedCategoryId = uiState.selectedCategoryId,
                            loading = uiState.productsLoading,
                            errors = uiState.errors,
                            onRefresh = onRefreshProducts,
                            onProductActionClick = onProductActionClick,
                            onProductSelected = onProductClick
                        )
                    },
                    rightPaneContent = { modifier, paddingValues ->
                        ProductDetailScreenContent(
                            modifier = modifier,
                            paddingValues = paddingValues,
                            product = uiState.selectedProduct,
                            loading = uiState.productsLoading,
                            errors = uiState.errors,
                            onRefresh = onRefreshProductDetail,
                            onProductActionClick = onProductActionClick
                        )
                    }
                )
                BackHandler {
                    onInteractWithProductDetail()
                }
            }
    }
}

/**
 * A precise enumeration of which type of screen to display at the home route.
 *
 * There are 5 options:
 * - [Categories], which displays just a list of all categories.
 * - [Products], which displays just a list of products from a specific category.
 * - [ProductDetail], which displays just a specific product.
 * - [CategoriesWithProducts], which displays a list of all categories and a list of all products
 * belonging to the selected category.
 * - [ProductsWithProductDetail], which displays a list of products from a specific category and
 * detail of a specific product.
 * - [CategoriesWithProductsWithProductDetail], which displays a list of all categories, the
 * selected category's products and a specific product.
 */
private enum class HomeScreenType {
    Categories,
    Products,
    ProductDetail,
    CategoriesWithProducts,
    ProductsWithProductDetail,
    CategoriesWithProductsWithProductDetail
}

/**
 * Returns the current [HomeScreenType] to display, based on whether or not the screen is expanded
 * and the [HomeUiState].
 */
@Composable
private fun getHomeScreenType(
    screenWidthSize: WindowInfo.WindowSize,
    uiState: HomeUiState
): HomeScreenType = when (screenWidthSize) {
    WindowInfo.WindowSize.Compact -> {
        when (uiState) {
            is HomeUiState.HasCategoriesHasProductsHasProductDetail -> HomeScreenType.ProductDetail
            is HomeUiState.HasCategoriesHasProductsNoProductDetail -> HomeScreenType.ProductDetail
            is HomeUiState.HasCategoriesHasProducts -> HomeScreenType.Products
            is HomeUiState.HasCategoriesNoProducts -> HomeScreenType.Products
            is HomeUiState.HasCategories -> HomeScreenType.Categories
            is HomeUiState.NoCategories -> HomeScreenType.Categories
        }
    }
    WindowInfo.WindowSize.Medium -> {
        when (uiState) {
            is HomeUiState.HasCategoriesHasProductsHasProductDetail ->
                HomeScreenType.ProductsWithProductDetail
            is HomeUiState.HasCategoriesHasProductsNoProductDetail ->
                HomeScreenType.ProductsWithProductDetail
            is HomeUiState.HasCategoriesHasProducts -> HomeScreenType.CategoriesWithProducts
            is HomeUiState.HasCategoriesNoProducts -> HomeScreenType.CategoriesWithProducts
            is HomeUiState.HasCategories -> HomeScreenType.Categories
            is HomeUiState.NoCategories -> HomeScreenType.Categories
        }
    }
    WindowInfo.WindowSize.Expanded -> {
        when (uiState) {
            is HomeUiState.HasCategoriesHasProductsHasProductDetail ->
                HomeScreenType.CategoriesWithProductsWithProductDetail
            is HomeUiState.HasCategoriesHasProductsNoProductDetail ->
                HomeScreenType.CategoriesWithProductsWithProductDetail
            is HomeUiState.HasCategoriesHasProducts -> HomeScreenType.CategoriesWithProducts
            is HomeUiState.HasCategoriesNoProducts -> HomeScreenType.CategoriesWithProducts
            is HomeUiState.HasCategories -> HomeScreenType.Categories
            is HomeUiState.NoCategories -> HomeScreenType.Categories
        }
    }
}