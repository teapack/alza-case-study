package cz.vratislavjindra.alzacasestudy.feature_products.presentation.product_detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import cz.vratislavjindra.alzacasestudy.R
import cz.vratislavjindra.alzacasestudy.core.util.AlzaError
import cz.vratislavjindra.alzacasestudy.feature_products.domain.model.Product
import cz.vratislavjindra.alzacasestudy.ui.components.AlzaScaffold
import cz.vratislavjindra.alzacasestudy.ui.components.layout.EmptyPanePlaceholder
import cz.vratislavjindra.alzacasestudy.ui.components.loading.LoadingIndicatorFullscreen
import cz.vratislavjindra.alzacasestudy.ui.components.product.action.BuyProductAction
import cz.vratislavjindra.alzacasestudy.ui.components.product.attribute.*
import cz.vratislavjindra.alzacasestudy.ui.components.top_app_bar.TopAppBarAction

@Composable
fun ProductDetailScreen(
    product: Product,
    loading: Boolean,
    errors: List<AlzaError>,
    onUpClick: () -> Unit,
    onRefresh: () -> Unit,
    onProductActionClick: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    AlzaScaffold(
        modifier = Modifier,
        snackbarHostState = snackbarHostState,
        topBarTitle = stringResource(id = R.string.title_product_detail),
        upNavigationAction = onUpClick,
        topAppBarActions = listOf(
            TopAppBarAction(
                icon = Icons.Rounded.Refresh,
                rotateIcon = loading,
                contentDescription = stringResource(
                    id = R.string.content_description_button_refresh_product_detail
                )
            ) { onRefresh() }
        ),
//        snackbarFlow = viewModel.snackbarFlow
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            ProductDetailScreenContent(
                modifier = Modifier,
                paddingValues = paddingValues,
                product = product,
                loading = loading,
                errors = errors,
                onRefresh = onRefresh,
                onProductActionClick = onProductActionClick
            )
        }
    }
}

@Composable
fun NoProductDetailScreen(
    loading: Boolean,
    errors: List<AlzaError>,
    onUpClick: () -> Unit,
    onRefresh: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    AlzaScaffold(
        modifier = Modifier,
        snackbarHostState = snackbarHostState,
        topBarTitle = stringResource(id = R.string.title_product_detail),
        upNavigationAction = onUpClick,
        topAppBarActions = listOf(
            TopAppBarAction(
                icon = Icons.Rounded.Refresh,
                rotateIcon = loading,
                contentDescription = stringResource(
                    id = R.string.content_description_button_refresh_product_detail
                )
            ) { onRefresh() }
        ),
//        snackbarFlow = viewModel.snackbarFlow
    ) { paddingValues ->
        NoProductDetailScreenContent(
            modifier = Modifier,
            paddingValues = paddingValues,
            loading = loading,
            errors = errors,
            onRefresh = onRefresh
        )
    }
}

@Composable
fun NoProductDetailScreenContent(
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
            text = "No product detail\nLoading: $loading\nErrors: $errors"
        )
    }
}

@Composable
fun ProductDetailScreenContent(
    modifier: Modifier,
    paddingValues: PaddingValues,
    product: Product,
    loading: Boolean,
    errors: List<AlzaError>,
    onRefresh: () -> Unit,
    onProductActionClick: () -> Unit
) {
    if (loading) {
        LoadingIndicatorFullscreen(
            modifier = modifier,
            paddingValues = paddingValues
        )
    } else {
        Column(modifier = modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .weight(weight = 1f)
                    .verticalScroll(state = rememberScrollState())
            ) {
                ProductTitle(
                    title = product.name,
                    modifier = Modifier
                        .padding(all = 16.dp)
                        .padding(top = paddingValues.calculateTopPadding()),
                    small = false
                )
                StarRating(
                    rating = product.rating,
                    small = false,
                    modifier = Modifier.padding(horizontal = 10.dp)
                )
                Spacer(modifier = Modifier.height(height = 16.dp))
                product.imageUrl?.let { imageUrl ->
                    AsyncImage(
                        model = ImageRequest.Builder(context = LocalContext.current)
                            .data(data = imageUrl)
                            .crossfade(enable = true)
                            .build(),
                        contentDescription = product.name,
                        modifier = Modifier.aspectRatio(ratio = 1f)
                    )
                    Spacer(modifier = Modifier.height(height = 16.dp))
                }
                Row(modifier = Modifier.padding(horizontal = 16.dp)) {
                    ProductAvailability(
                        availability = product.availability,
                        available = product.canBuy,
                        small = false,
                        modifier = Modifier.weight(weight = 1f)
                    )
                }
                ProductDescription(
                    description = product.description,
                    short = false,
                    modifier = Modifier.padding(all = 16.dp)
                )
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
                        ProductPrice(price = price, small = false)
                    }
                    Spacer(
                        modifier = Modifier
                            .width(width = 16.dp)
                            .weight(weight = 1f)
                    )
                    BuyProductAction(onClick = onProductActionClick)
                }
            }
        }
    }
}