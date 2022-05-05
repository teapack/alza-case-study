package cz.vratislavjindra.alzacasestudy.feature_products.presentation.product_categories

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import cz.vratislavjindra.alzacasestudy.ui.common.list.CardListItem
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
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
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
        columns = GridCells.Adaptive(minSize = 172.dp),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = paddingValues.calculateStartPadding(layoutDirection = LayoutDirection.Ltr) + 8.dp,
            top = paddingValues.calculateTopPadding() + 8.dp,
            end = paddingValues.calculateEndPadding(layoutDirection = LayoutDirection.Rtl) + 8.dp,
            bottom = navigationBarsPadding + 8.dp
        )
    ) {

        items(items = categories) { category ->
            CardListItem(
                title = category.name,
                imageUrl = category.imageUrl,
                modifier = Modifier.padding(all = 8.dp)
            ) { onCategoryClick(category) }
        }
    }
//    LazyColumn {
//        itemsIndexed(items = categories) { index, category ->
//            if (index == 0) {
//                Spacer(modifier = Modifier.height(height = paddingValues.calculateTopPadding()))
//            }
//            CardListItem(
//                title = category.name,
//                imageUrl = category.imageUrl,
//            ) { onCategoryClick(category) }
//            if (index == categories.size - 1) {
//                val navigationBarsPadding = WindowInsets.navigationBars.only(
//                    sides = WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom
//                ).asPaddingValues().calculateBottomPadding()
//                Spacer(modifier = Modifier.height(height = navigationBarsPadding + 16.dp))
//            }
//        }
//    }
}