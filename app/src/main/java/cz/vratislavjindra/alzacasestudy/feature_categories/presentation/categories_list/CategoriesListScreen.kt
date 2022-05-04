package cz.vratislavjindra.alzacasestudy.feature_categories.presentation.categories_list

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import cz.vratislavjindra.alzacasestudy.R
import cz.vratislavjindra.alzacasestudy.feature_categories.domain.model.Category
import cz.vratislavjindra.alzacasestudy.ui.common.AlzaScaffold
import cz.vratislavjindra.alzacasestudy.ui.common.top_app_bar.TopAppBarAction

@Composable
fun CategoriesListScreen(
    navController: NavController,
    viewModel: CategoriesListViewModel = hiltViewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val topAppBarActions = mutableListOf(
        TopAppBarAction(
            icon = Icons.Rounded.Refresh,
            contentDescription = stringResource(
                id = R.string.content_description_button_refresh_categories
            )
        ) { viewModel.getAllCategories() }
    )
    if (viewModel.state.value.loading) {
        topAppBarActions.add(
            TopAppBarAction(
                icon = Icons.Rounded.Share,
                contentDescription = stringResource(
                    id = R.string.content_description_button_refresh_categories
                )
            ) { /* NO-OP */ }
        )
    }
    AlzaScaffold(
        navController = navController,
        snackbarHostState = snackbarHostState,
        topBarTitle = stringResource(id = R.string.title_categories),
        upNavigationAction = null,
        topAppBarActions = topAppBarActions.toList()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues = paddingValues)
                .fillMaxSize()
        ) {
            Text(
                text = "Loading: ${viewModel.state.value.loading}"
            )
            CategoriesList(categories = viewModel.state.value.categories)
        }
    }
}

@Composable
private fun CategoriesList(categories: List<Category>) {
    LazyColumn {
        items(items = categories) { category ->
            CategoryListItem(category = category)
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun CategoryListItem(category: Category) {
    Card(
        onClick = {
            // TODO Handle the onClick action.
            android.util.Log.d("----------", "Category: $category")
        },
        modifier = Modifier
            .padding(all = 8.dp)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(all = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            category.imageUrl?.let {
                AsyncImage(
                    model = ImageRequest.Builder(context = LocalContext.current)
                        .data(data = category.imageUrl)
                        .crossfade(enable = true)
                        .build(),
                    contentDescription = category.name
                )
                Spacer(modifier = Modifier.width(width = 16.dp))
            }
            Text(
                text = category.name,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}