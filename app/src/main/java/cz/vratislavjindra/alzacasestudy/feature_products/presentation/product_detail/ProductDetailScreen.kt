package cz.vratislavjindra.alzacasestudy.feature_products.presentation.product_detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import cz.vratislavjindra.alzacasestudy.R
import cz.vratislavjindra.alzacasestudy.ui.common.AlzaScaffold
import cz.vratislavjindra.alzacasestudy.ui.common.product_attribute.*
import cz.vratislavjindra.alzacasestudy.ui.common.top_app_bar.TopAppBarAction

@Composable
fun ProductDetailScreen(
    navController: NavController,
    viewModel: ProductDetailViewModel = hiltViewModel(),
    productId: Int?,
    productName: String?
) {
    val snackbarHostState = remember { SnackbarHostState() }
    AlzaScaffold(
        navController = navController,
        snackbarHostState = snackbarHostState,
        topBarTitle = productName ?: stringResource(id = R.string.title_product_detail),
        topAppBarActions = listOf(
            TopAppBarAction(
                icon = Icons.Rounded.Refresh,
                rotateIcon = viewModel.state.value.loading,
                contentDescription = stringResource(
                    id = R.string.content_description_button_refresh_product_detail
                )
            ) { viewModel.getProductDetail(productId = productId) }
        ),
        snackbarFlow = viewModel.snackbarFlow
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            viewModel.state.value.product?.let { product ->
                ProductDetail(
                    name = product.name,
                    description = product.description,
                    imageUrl = product.imageUrl,
                    price = product.price,
                    availability = product.availability,
                    canBuy = product.canBuy,
                    rating = product.rating,
                    paddingValues = paddingValues,
                    onProductActionClick = viewModel::onProductActionClick
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
private fun ProductDetail(
    name: String,
    description: String,
    imageUrl: String?,
    price: String?,
    availability: String,
    canBuy: Boolean,
    rating: Float,
    paddingValues: PaddingValues,
    onProductActionClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(state = rememberScrollState())
    ) {
        ProductTitle(
            title = name,
            modifier = Modifier.padding(
                start = 16.dp,
                top = paddingValues.calculateTopPadding() + 16.dp,
                end = 16.dp,
                bottom = 16.dp
            ),
            small = false
        )
        StarRating(
            rating = rating,
            small = false,
            modifier = Modifier.padding(horizontal = 10.dp)
        )
        Spacer(modifier = Modifier.height(height = 16.dp))
        imageUrl?.let {
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(data = it)
                    .crossfade(enable = true)
                    .build(),
                contentDescription = name,
                modifier = Modifier.aspectRatio(ratio = 1f)
            )
            Spacer(modifier = Modifier.height(height = 16.dp))
        }
        Row(modifier = Modifier.padding(horizontal = 16.dp)) {
            ProductAvailability(
                availability = availability,
                available = canBuy,
                small = false,
                modifier = Modifier.weight(weight = 1f)
            )
            price?.let {
                Spacer(modifier = Modifier.width(width = 16.dp))
                ProductPrice(
                    price = price,
                    small = false
                )
            }
        }
        val navigationBarsPadding = WindowInsets.navigationBars.only(
            sides = WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom
        ).asPaddingValues().calculateBottomPadding()
        ProductDescription(
            description = description,
            short = false,
            modifier = Modifier.padding(
                start = 16.dp,
                top = 16.dp,
                end = 16.dp,
                bottom = navigationBarsPadding + 16.dp
            )
        )
    }
}