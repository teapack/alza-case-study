package cz.vratislavjindra.alzacasestudy.feature_products.presentation.products

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cz.vratislavjindra.alzacasestudy.R
import cz.vratislavjindra.alzacasestudy.core.util.AlzaError
import cz.vratislavjindra.alzacasestudy.feature_products.domain.model.ProductListItem
import cz.vratislavjindra.alzacasestudy.ui.components.AlzaScaffold
import cz.vratislavjindra.alzacasestudy.ui.components.card.SurfaceCard
import cz.vratislavjindra.alzacasestudy.ui.components.layout.EmptyPanePlaceholder
import cz.vratislavjindra.alzacasestudy.ui.components.list.ItemDivider
import cz.vratislavjindra.alzacasestudy.ui.components.list.ItemImage
import cz.vratislavjindra.alzacasestudy.ui.components.loading.LoadingIndicatorFullscreen
import cz.vratislavjindra.alzacasestudy.ui.components.product.action.BuyProductAction
import cz.vratislavjindra.alzacasestudy.ui.components.product.attribute.*
import cz.vratislavjindra.alzacasestudy.ui.components.top_app_bar.TopAppBarAction

@Composable
fun ProductsScreen(
    productsLazyGridStates: Map<Int, LazyGridState>,
    products: List<ProductListItem>,
    selectedProductId: Int?,
    selectedCategoryId: Int,
    loading: Boolean,
    errors: List<AlzaError>,
    onUpClick: () -> Unit,
    onRefresh: () -> Unit,
    onProductActionClick: () -> Unit,
    onProductSelected: (productId: Int) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    AlzaScaffold(
        modifier = Modifier,
        snackbarHostState = snackbarHostState,
        topBarTitle = stringResource(id = R.string.title_products),
        upNavigationAction = onUpClick,
        topAppBarActions = listOf(
            TopAppBarAction(
                icon = Icons.Rounded.Refresh,
                rotateIcon = loading,
                contentDescription = stringResource(
                    id = R.string.content_description_button_refresh_products
                )
            ) { onRefresh() }
        ),
//        snackbarFlow = viewModel.snackbarFlow
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            ProductsScreenContent(
                modifier = Modifier,
                productsLazyGridStates = productsLazyGridStates,
                paddingValues = paddingValues,
                products = products,
                selectedProductId = selectedProductId,
                selectedCategoryId = selectedCategoryId,
                loading = loading,
                errors = errors,
                onRefresh = onRefresh,
                onProductActionClick = onProductActionClick
            ) { onProductSelected(it) }
        }
    }
}

@Composable
fun NoProductsScreen(
    loading: Boolean,
    errors: List<AlzaError>,
    onUpClick: () -> Unit,
    onRefresh: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    AlzaScaffold(
        modifier = Modifier,
        snackbarHostState = snackbarHostState,
        topBarTitle = stringResource(id = R.string.title_products),
        upNavigationAction = onUpClick,
        topAppBarActions = listOf(
            TopAppBarAction(
                icon = Icons.Rounded.Refresh,
                rotateIcon = loading,
                contentDescription = stringResource(
                    id = R.string.content_description_button_refresh_products
                )
            ) { onRefresh() }
        ),
//        snackbarFlow = viewModel.snackbarFlow
    ) { paddingValues ->
        NoProductsScreenContent(
            modifier = Modifier,
            paddingValues = paddingValues,
            loading = loading,
            errors = errors,
            onRefresh = onRefresh
        )
    }
}

@Composable
fun NoProductsScreenContent(
    modifier: Modifier,
    paddingValues: PaddingValues,
    loading: Boolean,
    errors: List<AlzaError>,
    onRefresh: () -> Unit
) {
    if (loading) {
        LoadingIndicatorFullscreen(
            modifier = modifier,
            paddingValues = paddingValues
        )
    } else {
        EmptyPanePlaceholder(
            modifier = modifier,
            paddingValues = paddingValues,
            text = "No products\nLoading: $loading\nErrors: $errors"
        )
    }
}

@Composable
fun ProductsScreenContent(
    modifier: Modifier,
    productsLazyGridStates: Map<Int, LazyGridState>,
    paddingValues: PaddingValues,
    products: List<ProductListItem>,
    selectedCategoryId: Int,
    selectedProductId: Int?,
    loading: Boolean,
    errors: List<AlzaError>,
    onRefresh: () -> Unit,
    onProductActionClick: () -> Unit,
    onProductSelected: (productId: Int) -> Unit
) {
    if (loading && products.isEmpty()) {
        LoadingIndicatorFullscreen(
            modifier = modifier,
            paddingValues = paddingValues
        )
    } else {
        if (products.isNotEmpty()) {
            val navigationBarsPadding = WindowInsets.navigationBars.only(
                sides = WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom
            ).asPaddingValues().calculateBottomPadding()
            // Get the lazy grid state for the selected category.
            val productsLazyGridState by derivedStateOf {
                productsLazyGridStates.getValue(key = selectedCategoryId)
            }
            key(selectedCategoryId) {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 320.dp),
                    modifier = modifier.fillMaxSize(),
                    state = productsLazyGridState,
                    contentPadding = PaddingValues(
                        start = 8.dp,
                        top = paddingValues.calculateTopPadding() + 8.dp,
                        end = 8.dp,
                        bottom = navigationBarsPadding + 8.dp
                    )
                ) {
                    items(items = products) { product ->
                        ProductCard(
                            selected = selectedProductId != null && selectedProductId == product.id,
                            name = product.name,
                            description = product.description,
                            imageUrl = product.imageUrl,
                            price = product.price,
                            availability = product.availability,
                            canBuy = product.canBuy,
                            rating = product.rating,
                            modifier = Modifier.padding(all = 8.dp),
                            onProductActionClick = onProductActionClick
                        ) { onProductSelected(product.id) }
                    }
                }
            }
        }
        if (loading) {
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(paddingValues = paddingValues)
            )
        }
    }
}

@Composable
private fun ProductCard(
    selected: Boolean,
    name: String,
    description: String,
    imageUrl: String?,
    price: String?,
    availability: String,
    canBuy: Boolean,
    rating: Float,
    modifier: Modifier = Modifier,
    onProductActionClick: () -> Unit,
    onClick: () -> Unit
) {
    SurfaceCard(
        modifier = modifier,
        selected = selected,
        onClick = onClick
    ) {
        Column {
            Row(
                modifier = Modifier.padding(all = 16.dp),
                verticalAlignment = Alignment.Top
            ) {
                imageUrl?.let {
                    ItemImage(
                        imageUrl = imageUrl,
                        contentDescription = name,
                        modifier = Modifier
                            .weight(weight = 2f)
                            .aspectRatio(ratio = 1f)
                            .fillMaxSize()
                    )
                    Spacer(modifier = Modifier.width(width = 16.dp))
                }
                Column(modifier = Modifier.weight(weight = 3f)) {
                    ProductTitle(
                        title = name,
                        maxLines = 3,
                        small = true
                    )
                    Spacer(modifier = Modifier.height(height = 4.dp))
                    StarRating(
                        rating = rating,
                        small = true
                    )
                    price?.let {
                        Spacer(modifier = Modifier.height(height = 8.dp))
                        ProductPrice(
                            price = price,
                            small = true
                        )
                    }
                    Spacer(modifier = Modifier.height(height = 8.dp))
                    ProductAvailability(
                        availability = availability,
                        available = canBuy,
                        small = true
                    )
                }
            }
            ProductDescription(
                description = description,
                short = true,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(height = 16.dp))
            ItemDivider()
            Row {
                IconButton(onClick = onProductActionClick) {
                    Icon(
                        imageVector = Icons.Rounded.FavoriteBorder,
                        contentDescription = stringResource(
                            id = R.string.content_description_button_add_to_favorites
                        )
                    )
                }
                IconButton(onClick = onProductActionClick) {
                    Icon(
                        imageVector = Icons.Rounded.Info,
                        contentDescription = stringResource(
                            id = R.string.content_description_button_add_to_comparison
                        )
                    )
                }
                IconButton(onClick = onProductActionClick) {
                    Icon(
                        imageVector = Icons.Rounded.Share,
                        contentDescription = stringResource(
                            id = R.string.content_description_button_share
                        )
                    )
                }
                Spacer(modifier = Modifier.weight(weight = 1f))
                BuyProductAction(onClick = onProductActionClick)
            }
        }
    }
}