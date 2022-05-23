package cz.vratislavjindra.alzacasestudy.feature_products.presentation

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cz.vratislavjindra.alzacasestudy.R
import cz.vratislavjindra.alzacasestudy.core.utils.ErrorMessage
import cz.vratislavjindra.alzacasestudy.core.utils.isScrolled
import cz.vratislavjindra.alzacasestudy.feature_products.domain.model.Product
import cz.vratislavjindra.alzacasestudy.feature_products.presentation.product_detail.NoProductDetailScreenContent
import cz.vratislavjindra.alzacasestudy.feature_products.presentation.product_detail.ProductDetailScreenContent
import cz.vratislavjindra.alzacasestudy.feature_products.presentation.products_list.NoProductsScreenContent
import cz.vratislavjindra.alzacasestudy.feature_products.presentation.products_list.ProductsScreenContent
import cz.vratislavjindra.alzacasestudy.ui.components.error.ErrorFullscreen
import cz.vratislavjindra.alzacasestudy.ui.components.layout.AlzaScaffold
import cz.vratislavjindra.alzacasestudy.ui.components.layout.EmptyContentPlaceholder
import cz.vratislavjindra.alzacasestudy.ui.components.snackbar.SnackbarData
import kotlinx.coroutines.flow.Flow

@Composable
fun ProductsListWithProductDetailScreen(
    productsLazyGridState: LazyGridState,
    uiState: ProductsUiState,
    snackbarFlow: Flow<SnackbarData>,
    onRefreshProducts: () -> Unit,
    onSelectProduct: (Int) -> Unit,
    onProductActionClick: () -> Unit,
    onRefreshProductDetail: () -> Unit,
    onBack: () -> Unit
) {
    when (uiState) {
        is ProductsUiState.HasProducts -> {
            val snackbarHostState = remember { SnackbarHostState() }
            AlzaScaffold(
                modifier = Modifier,
                snackbarHostState = snackbarHostState,
                topBarTitle = stringResource(id = R.string.title_products),
                upNavigationAction = onBack,
                snackbarFlow = snackbarFlow,
                contentIsScrolled = true
            ) { paddingValues ->
                Box(modifier = Modifier.fillMaxSize()) {
                    Row {
                        ProductsScreenContent(
                            modifier = Modifier.weight(weight = 1f),
                            lazyGridState = productsLazyGridState,
                            paddingValues = paddingValues,
                            products = uiState.products,
                            loading = uiState.productsLoading,
                            onRefresh = onRefreshProducts,
                            selectedProductId = uiState.selectedProduct?.id,
                            onProductSelected = { onSelectProduct(it) },
                            onProductActionClick = onProductActionClick
                        )
                        // Divider between list and detail.
                        Surface(
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(width = 8.dp)
                                .padding(paddingValues = paddingValues),
                            shadowElevation = 2.dp
                        ) {}
                        // Cross-fade between different selected products.
                        Crossfade(
                            targetState = uiState.selectedProduct,
                            modifier = Modifier.weight(weight = 1f)
                        ) { selectedProduct ->
                            if (selectedProduct != null) {
                                ProductDetailScreenContent(
                                    modifier = Modifier.fillMaxSize(),
                                    paddingValues = paddingValues,
                                    contentScrollState = rememberScrollState(),
                                    product = selectedProduct,
                                    loading = uiState.productDetailLoading,
                                    onRefresh = onRefreshProductDetail,
                                    onProductActionClick = onProductActionClick
                                )
                            } else {
                                // TODO Make a better 'no product selected screen' content.
                                EmptyContentPlaceholder(
                                    modifier = Modifier,
                                    paddingValues = paddingValues,
                                    text = "Select a product on the left"
                                )
                            }
                        }
                    }
                }
            }
        }
        is ProductsUiState.NoProducts -> {
            ProductsListScreen(
                productsLazyGridState = productsLazyGridState,
                uiState = uiState,
                snackbarFlow = snackbarFlow,
                onRefresh = onRefreshProducts,
                onSelectProduct = onSelectProduct,
                onProductActionClick = onProductActionClick,
                onBack = onBack
            )
        }
    }
}

@Composable
fun ProductsListScreen(
    productsLazyGridState: LazyGridState,
    uiState: ProductsUiState,
    snackbarFlow: Flow<SnackbarData>,
    onRefresh: () -> Unit,
    onSelectProduct: (Int) -> Unit,
    onProductActionClick: () -> Unit,
    onBack: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    AlzaScaffold(
        modifier = Modifier,
        snackbarHostState = snackbarHostState,
        topBarTitle = stringResource(id = R.string.title_products),
        upNavigationAction = onBack,
        snackbarFlow = snackbarFlow,
        contentIsScrolled = productsLazyGridState.isScrolled
    ) { paddingValues ->
        uiState.productsListErrorMessage.let { errorMessage ->
            if (errorMessage != null) {
                ErrorFullscreen(
                    paddingValues = paddingValues,
                    errorMessage = errorMessage,
                    onTryAgainButtonClick = onRefresh
                )
            } else {
                when (uiState) {
                    is ProductsUiState.HasProducts -> ProductsScreenContent(
                        modifier = Modifier,
                        lazyGridState = productsLazyGridState,
                        paddingValues = paddingValues,
                        products = uiState.products,
                        loading = uiState.productsLoading,
                        onRefresh = onRefresh,
                        selectedProductId = uiState.selectedProduct?.id,
                        onProductSelected = { onSelectProduct(it) },
                        onProductActionClick = onProductActionClick
                    )
                    is ProductsUiState.NoProducts -> NoProductsScreenContent(
                        paddingValues = paddingValues,
                        onRefresh = onRefresh
                    )
                }
            }
        }
    }
}

@Composable
fun ProductDetailScreen(
    snackbarFlow: Flow<SnackbarData>,
    product: Product?,
    loading: Boolean,
    errorMessage: ErrorMessage?,
    onRefresh: () -> Unit,
    onProductActionClick: () -> Unit,
    onBack: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val contentScrollState = rememberScrollState()
    AlzaScaffold(
        modifier = Modifier,
        snackbarHostState = snackbarHostState,
        topBarTitle = stringResource(id = R.string.title_product_detail),
        upNavigationAction = onBack,
        snackbarFlow = snackbarFlow,
        contentIsScrolled = contentScrollState.isScrolled
    ) { paddingValues ->
        if (errorMessage != null) {
            ErrorFullscreen(
                paddingValues = paddingValues,
                errorMessage = errorMessage,
                onTryAgainButtonClick = onRefresh
            )
        } else if (product == null) {
            NoProductDetailScreenContent(
                paddingValues = paddingValues,
                onRefresh = onRefresh
            )
        } else {
            ProductDetailScreenContent(
                modifier = Modifier,
                paddingValues = paddingValues,
                contentScrollState = contentScrollState,
                product = product,
                loading = loading,
                onRefresh = onRefresh,
                onProductActionClick = onProductActionClick
            )
        }
    }
}