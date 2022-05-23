package cz.vratislavjindra.alzacasestudy.feature_products.presentation.products_list

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cz.vratislavjindra.alzacasestudy.R
import cz.vratislavjindra.alzacasestudy.feature_products.domain.model.Product
import cz.vratislavjindra.alzacasestudy.ui.components.card.SurfaceCardSelectable
import cz.vratislavjindra.alzacasestudy.ui.components.image.ItemImage
import cz.vratislavjindra.alzacasestudy.ui.components.layout.EmptyContentPlaceholderWithButton
import cz.vratislavjindra.alzacasestudy.ui.components.layout.SwipeRefreshLayout
import cz.vratislavjindra.alzacasestudy.ui.components.list.ItemDivider
import cz.vratislavjindra.alzacasestudy.ui.components.loading.shimmerPlaceholder
import cz.vratislavjindra.alzacasestudy.ui.components.product.action.BuyProductAction
import cz.vratislavjindra.alzacasestudy.ui.components.product.attribute.*

@Composable
fun ProductsScreenContent(
    modifier: Modifier,
    lazyGridState: LazyGridState,
    paddingValues: PaddingValues,
    products: List<Product>,
    loading: Boolean,
    onRefresh: () -> Unit,
    selectedProductId: Int?,
    onProductSelected: (productId: Int) -> Unit,
    onProductActionClick: () -> Unit
) {
    if (!loading && products.isEmpty()) {
        // We're not loading data anymore and no products were returned. It's not an error, though,
        // there are just no products available for the selected category.
        NoProductsScreenContent(
            paddingValues = paddingValues,
            onRefresh = onRefresh
        )
    } else {
        val navigationBarsPadding = WindowInsets.navigationBars.only(
            sides = WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom
        ).asPaddingValues().calculateBottomPadding()
        SwipeRefreshLayout(
            isRefreshing = loading,
            onRefresh = onRefresh,
            modifier = modifier.fillMaxSize(),
            paddingValues = paddingValues
        ) {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 320.dp),
                state = lazyGridState,
                contentPadding = PaddingValues(
                    start = 8.dp,
                    top = paddingValues.calculateTopPadding() + 8.dp,
                    end = 8.dp,
                    bottom = navigationBarsPadding + 8.dp
                )
            ) {
                // TODO I have never done skeleton UIs before, this is my first shot at it. I guess it could be done better than passing dummy skeleton products here.
                items(
                    items = products.ifEmpty {
                        val skeletonProducts = mutableListOf<Product>()
                        for (i in 0..24) {
                            skeletonProducts.add(
                                element = Product(
                                    id = i,
                                    name = "\n",
                                    description = "\n\n\n\n",
                                    imageUrl = null,
                                    price = null,
                                    availability = "",
                                    canBuy = false,
                                    rating = 0f,
                                    order = i,
                                    categoryId = 0
                                )
                            )
                        }
                        skeletonProducts.toList()
                    }
                ) { product ->
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
                        loading = loading,
                        onProductActionClick = onProductActionClick
                    ) { onProductSelected(product.id) }
                }
            }
        }
    }
}

@Composable
fun NoProductsScreenContent(
    paddingValues: PaddingValues,
    onRefresh: () -> Unit
) {
    EmptyContentPlaceholderWithButton(
        modifier = Modifier,
        paddingValues = paddingValues,
        text = stringResource(id = R.string.title_no_products_available),
        buttonText = stringResource(id = R.string.button_refresh),
        buttonAction = onRefresh
    )
}

@Composable
fun ProductCard(
    selected: Boolean,
    name: String,
    description: String,
    imageUrl: String?,
    price: String?,
    availability: String,
    canBuy: Boolean,
    rating: Float,
    modifier: Modifier = Modifier,
    loading: Boolean,
    onProductActionClick: () -> Unit,
    onClick: () -> Unit
) {
    SurfaceCardSelectable(
        modifier = modifier.shimmerPlaceholder(visible = loading),
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