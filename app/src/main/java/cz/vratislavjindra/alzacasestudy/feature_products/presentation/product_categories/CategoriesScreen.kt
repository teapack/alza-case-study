package cz.vratislavjindra.alzacasestudy.feature_products.presentation.product_categories

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import cz.vratislavjindra.alzacasestudy.R
import cz.vratislavjindra.alzacasestudy.core.util.AlzaError
import cz.vratislavjindra.alzacasestudy.feature_products.domain.model.Category
import cz.vratislavjindra.alzacasestudy.ui.components.AlzaScaffold
import cz.vratislavjindra.alzacasestudy.ui.components.card.SurfaceCard
import cz.vratislavjindra.alzacasestudy.ui.components.layout.EmptyPanePlaceholder
import cz.vratislavjindra.alzacasestudy.ui.components.list.ItemImage
import cz.vratislavjindra.alzacasestudy.ui.components.list.ListItemTitle
import cz.vratislavjindra.alzacasestudy.ui.components.loading.LoadingIndicatorFullscreen
import cz.vratislavjindra.alzacasestudy.ui.components.top_app_bar.TopAppBarAction

@Composable
fun CategoriesScreen(
    categoriesLazyGridState: LazyGridState,
    categories: List<Category>,
    selectedCategoryId: Int?,
    loading: Boolean,
    errors: List<AlzaError>,
    onRefresh: () -> Unit,
    onCategorySelected: (categoryId: Int) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    AlzaScaffold(
        modifier = Modifier,
        snackbarHostState = snackbarHostState,
        topBarTitle = stringResource(id = R.string.title_categories),
        upNavigationAction = null,
        topAppBarActions = listOf(
            TopAppBarAction(
                icon = Icons.Rounded.Refresh,
                rotateIcon = loading,
                contentDescription = stringResource(
                    id = R.string.content_description_button_refresh_categories
                )
            ) { onRefresh() }
        ),
//        snackbarFlow = viewModel.snackbarDataFlow
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            CategoriesScreenContent(
                modifier = Modifier,
                categoriesLazyGridState = categoriesLazyGridState,
                paddingValues = paddingValues,
                categories = categories,
                selectedCategoryId = selectedCategoryId,
                loading = loading,
                errors = errors,
                onRefresh = onRefresh
            ) { onCategorySelected(it) }
        }
    }
}

@Composable
fun NoCategoriesScreen(
    loading: Boolean,
    errors: List<AlzaError>,
    onRefresh: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    AlzaScaffold(
        modifier = Modifier,
        snackbarHostState = snackbarHostState,
        topBarTitle = stringResource(id = R.string.title_categories),
        upNavigationAction = null,
        topAppBarActions = listOf(
            TopAppBarAction(
                icon = Icons.Rounded.Refresh,
                rotateIcon = loading,
                contentDescription = stringResource(
                    id = R.string.content_description_button_refresh_categories
                )
            ) { onRefresh() }
        ),
//        snackbarFlow = viewModel.snackbarDataFlow
    ) { paddingValues ->
        NoCategoriesScreenContent(
            modifier = Modifier,
            paddingValues = paddingValues,
            loading = loading,
            errors = errors
        )

    }
}

@Composable
fun NoCategoriesScreenContent(
    modifier: Modifier,
    paddingValues: PaddingValues,
    loading: Boolean,
    errors: List<AlzaError>
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
            text = "No categories\nLoading: $loading\nErrors: $errors"
        )
    }
}

@Composable
fun CategoriesScreenContent(
    modifier: Modifier,
    categoriesLazyGridState: LazyGridState,
    paddingValues: PaddingValues,
    categories: List<Category>,
    selectedCategoryId: Int?,
    loading: Boolean,
    errors: List<AlzaError>,
    onRefresh: () -> Unit,
    onCategorySelected: (categoryId: Int) -> Unit
) {
    if (loading && categories.isEmpty()) {
        LoadingIndicatorFullscreen(
            modifier = modifier,
            paddingValues = paddingValues
        )
    } else {
        if (categories.isNotEmpty()) {
            val navigationBarsPadding = WindowInsets.navigationBars.only(
                sides = WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom
            ).asPaddingValues().calculateBottomPadding()
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 176.dp),
                modifier = modifier.fillMaxSize(),
                state = categoriesLazyGridState,
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
                        selected = selectedCategoryId != null && selectedCategoryId == category.id,
                        name = category.name,
                        imageUrl = category.imageUrl,
                        modifier = Modifier.padding(all = 8.dp)
                    ) { onCategorySelected(category.id) }
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
private fun CategoryCard(
    selected: Boolean,
    name: String,
    imageUrl: String?,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    SurfaceCard(
        modifier = modifier.height(height = 80.dp),
        selected = selected,
        onClick = onClick
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