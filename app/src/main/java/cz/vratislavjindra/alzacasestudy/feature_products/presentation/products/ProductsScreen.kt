package cz.vratislavjindra.alzacasestudy.feature_products.presentation.products

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import cz.vratislavjindra.alzacasestudy.R
import cz.vratislavjindra.alzacasestudy.feature_products.domain.model.ProductOverview
import cz.vratislavjindra.alzacasestudy.ui.common.AlzaScaffold
import cz.vratislavjindra.alzacasestudy.ui.common.list.ItemDivider
import cz.vratislavjindra.alzacasestudy.ui.common.list.SurfaceListItem
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
        snackbarFlow = viewModel.productClickedFlow
    ) { paddingValues ->
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            ProductList(
                products = viewModel.state.value.products,
                paddingValues = paddingValues
            ) {
                viewModel.onProductClick(product = it)
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
    products: List<ProductOverview>,
    paddingValues: PaddingValues,
    onProductClick: (product: ProductOverview) -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        itemsIndexed(items = products) { index, product ->
            if (index == 0) {
                Spacer(modifier = Modifier.height(height = paddingValues.calculateTopPadding()))
            }
            SurfaceListItem(
                title = product.name,
                imageUrl = null,
            ) { onProductClick(product) }
            if (index == products.size - 1) {
                // TODO The spacer height should match navigation bars height.
                Spacer(modifier = Modifier.height(height = 16.dp))
            } else {
                ItemDivider()
            }
        }
    }
}