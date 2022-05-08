package cz.vratislavjindra.alzacasestudy.feature_products.presentation.product_categories

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.SnackbarHostState
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
import cz.vratislavjindra.alzacasestudy.feature_products.domain.model.Category
import cz.vratislavjindra.alzacasestudy.ui.common.AlzaScaffold
import cz.vratislavjindra.alzacasestudy.ui.common.card.SurfaceCard
import cz.vratislavjindra.alzacasestudy.ui.common.list.ItemImage
import cz.vratislavjindra.alzacasestudy.ui.common.list.ListItemTitle
import cz.vratislavjindra.alzacasestudy.ui.common.top_app_bar.TopAppBarAction

@Composable
fun CategoriesScreen(
    navController: NavController,
    viewModel: CategoriesListViewModel = hiltViewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }
    AlzaScaffold(
        navController = navController,
        snackbarHostState = snackbarHostState,
        topBarTitle = stringResource(id = R.string.title_categories),
        upNavigationAction = null,
        topAppBarActions = listOf(
            TopAppBarAction(
                icon = Icons.Rounded.Refresh,
                rotateIcon = viewModel.state.value.loading,
                contentDescription = stringResource(
                    id = R.string.content_description_button_refresh_categories
                )
            ) { viewModel.getAllCategories() }
        ),
        snackbarFlow = viewModel.snackbarDataFlow
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            CategoryList(
                categories = viewModel.state.value.categories,
                paddingValues = paddingValues
            ) {
                navController.navigate(route = "${Screen.ProductsScreen.route}/${it.id}/${it.name}")
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
private fun CategoryList(
    categories: List<Category>,
    paddingValues: PaddingValues,
    onCategoryClick: (category: Category) -> Unit
) {
    val navigationBarsPadding = WindowInsets.navigationBars.only(
        sides = WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom
    ).asPaddingValues().calculateBottomPadding()
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 176.dp),
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
        items(items = categories) { category ->
            CategoryCard(
                name = category.name,
                imageUrl = category.imageUrl,
                modifier = Modifier.padding(all = 8.dp)
            ) { onCategoryClick(category) }
        }
    }
}

@Composable
private fun CategoryCard(
    name: String,
    imageUrl: String?,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    SurfaceCard(
        onClick = onClick,
        modifier = modifier.height(height = 80.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            imageUrl?.let {
                ItemImage(
                    imageUrl = it,
                    contentDescription = name,
                    modifier = Modifier.weight(weight = 1f)
                )
                Spacer(modifier = Modifier.width(width = 16.dp))
            }
            ListItemTitle(
                title = name,
                modifier = Modifier.weight(weight = 3f),
                maxLines = 2
            )
        }
    }
}