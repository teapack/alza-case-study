package cz.vratislavjindra.alzacasestudy.feature_products.presentation.product_detail

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cz.vratislavjindra.alzacasestudy.R
import cz.vratislavjindra.alzacasestudy.feature_products.domain.model.Product
import cz.vratislavjindra.alzacasestudy.ui.components.image.ItemImage
import cz.vratislavjindra.alzacasestudy.ui.components.layout.EmptyContentPlaceholderWithButton
import cz.vratislavjindra.alzacasestudy.ui.components.layout.SwipeRefreshLayout
import cz.vratislavjindra.alzacasestudy.ui.components.loading.shimmerPlaceholder
import cz.vratislavjindra.alzacasestudy.ui.components.product.action.BuyProductAction
import cz.vratislavjindra.alzacasestudy.ui.components.product.attribute.*

@Composable
fun ProductDetailScreenContent(
    modifier: Modifier,
    paddingValues: PaddingValues,
    contentScrollState: ScrollState,
    product: Product,
    loading: Boolean,
    onRefresh: () -> Unit,
    onProductActionClick: () -> Unit
) {
    Column(modifier = modifier.fillMaxSize()) {
        SwipeRefreshLayout(
            isRefreshing = loading,
            onRefresh = onRefresh,
            modifier = Modifier.weight(weight = 1f),
            paddingValues = paddingValues
        ) {
            Column(modifier = Modifier.verticalScroll(state = contentScrollState)) {
                ProductTitle(
                    title = product.name,
                    modifier = Modifier
                        .padding(all = 16.dp)
                        .padding(top = paddingValues.calculateTopPadding())
                        .shimmerPlaceholder(visible = loading),
                    small = false
                )
                StarRating(
                    rating = product.rating,
                    small = false,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .shimmerPlaceholder(visible = loading)
                )
                Spacer(modifier = Modifier.height(height = 16.dp))
                product.imageUrl?.let { imageUrl ->
                    ItemImage(
                        imageUrl = imageUrl,
                        contentDescription = product.name,
                        modifier = Modifier
                            .aspectRatio(ratio = 1f)
                            .shimmerPlaceholder(visible = loading)
                    )
                    Spacer(modifier = Modifier.height(height = 16.dp))
                }
                ProductAvailability(
                    availability = product.availability,
                    available = product.canBuy,
                    small = false,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .shimmerPlaceholder(visible = loading)
                )
                ProductDescription(
                    description = product.description,
                    short = false,
                    modifier = Modifier
                        .padding(all = 16.dp)
                        .shimmerPlaceholder(visible = loading)
                )
            }
        }
        val navigationBarsPadding = WindowInsets.navigationBars.only(
            sides = WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom
        ).asPaddingValues().calculateBottomPadding()
        Surface(shadowElevation = 16.dp) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .padding(bottom = navigationBarsPadding),
                verticalAlignment = Alignment.CenterVertically
            ) {
                product.price?.let { price ->
                    ProductPrice(
                        price = price,
                        small = false,
                        modifier = Modifier.shimmerPlaceholder(visible = loading)
                    )
                }
                Spacer(
                    modifier = Modifier
                        .width(width = 16.dp)
                        .weight(weight = 1f)
                )
                BuyProductAction(
                    onClick = onProductActionClick,
                    modifier = Modifier.shimmerPlaceholder(visible = loading)
                )
            }
        }
    }
}

@Composable
fun NoProductDetailScreenContent(
    paddingValues: PaddingValues,
    onRefresh: () -> Unit
) {
    EmptyContentPlaceholderWithButton(
        modifier = Modifier,
        paddingValues = paddingValues,
        text = stringResource(id = R.string.error_invalid_product),
        buttonText = stringResource(id = R.string.button_refresh),
        buttonAction = onRefresh
    )
}