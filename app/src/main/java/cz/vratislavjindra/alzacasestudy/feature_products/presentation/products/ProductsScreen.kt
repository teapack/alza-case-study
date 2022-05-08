package cz.vratislavjindra.alzacasestudy.feature_products.presentation.products

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import cz.vratislavjindra.alzacasestudy.R
import cz.vratislavjindra.alzacasestudy.Screen
import cz.vratislavjindra.alzacasestudy.feature_products.domain.model.ProductListItem
import cz.vratislavjindra.alzacasestudy.ui.common.AlzaScaffold
import cz.vratislavjindra.alzacasestudy.ui.common.card.SurfaceCard
import cz.vratislavjindra.alzacasestudy.ui.common.list.ItemDivider
import cz.vratislavjindra.alzacasestudy.ui.common.list.ItemImage
import cz.vratislavjindra.alzacasestudy.ui.common.product_attribute.*
import cz.vratislavjindra.alzacasestudy.ui.common.top_app_bar.TopAppBarAction

@Composable
fun ProductsScreen(
    navController: NavController,
    viewModel: ProductsViewModel = hiltViewModel(),
    categoryId: Int?,
    categoryName: String?
) {
    val snackbarHostState = remember { SnackbarHostState() }
    AlzaScaffold(
        navController = navController,
        snackbarHostState = snackbarHostState,
        topBarTitle = categoryName ?: stringResource(id = R.string.title_products),
        topAppBarActions = listOf(
            TopAppBarAction(
                icon = Icons.Rounded.Refresh,
                rotateIcon = viewModel.state.value.loading,
                contentDescription = stringResource(
                    id = R.string.content_description_button_refresh_products
                )
            ) { viewModel.getProducts(categoryId = categoryId) }
        ),
        snackbarFlow = viewModel.snackbarFlow
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            ProductList(
                products = viewModel.state.value.products,
                paddingValues = paddingValues,
                onProductActionClick = viewModel::onProductActionClick
            ) {
                navController.navigate(
                    route = "${Screen.ProductDetailScreen.route}/${it.id}/${it.name}"
                )
            }
            if (viewModel.state.value.loading) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(paddingValues = paddingValues)
                )
            }
        }
    }
}

@Composable
private fun ProductList(
    products: List<ProductListItem>,
    paddingValues: PaddingValues,
    onProductActionClick: () -> Unit,
    onProductClick: (product: ProductListItem) -> Unit
) {
    val navigationBarsPadding = WindowInsets.navigationBars.only(
        sides = WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom
    ).asPaddingValues().calculateBottomPadding()
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 320.dp),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = paddingValues.calculateStartPadding(
                layoutDirection = LayoutDirection.Ltr
            ) + 8.dp,
            top = paddingValues.calculateTopPadding() + 8.dp,
            end = paddingValues.calculateEndPadding(layoutDirection = LayoutDirection.Rtl) + 8.dp,
            bottom = navigationBarsPadding + 8.dp
        )
    ) {
        items(items = products) { product ->
            ProductCard(
                name = product.name,
                description = product.description,
                imageUrl = product.imageUrl,
                price = product.price,
                availability = product.availability,
                canBuy = product.canBuy,
                rating = product.rating,
                modifier = Modifier.padding(all = 8.dp),
                onProductActionClick = onProductActionClick
            ) { onProductClick(product) }
        }
    }
}

@Composable
private fun ProductCard(
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
        onClick = onClick,
        modifier = modifier
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
                TextButton(onClick = onProductActionClick) {
                    Icon(
                        imageVector = Icons.Rounded.ShoppingCart,
                        contentDescription = stringResource(
                            id = R.string.content_description_button_buy
                        )
                    )
                    Spacer(modifier = Modifier.width(width = 8.dp))
                    Text(text = stringResource(id = R.string.button_buy))
                }
            }
        }
    }
}