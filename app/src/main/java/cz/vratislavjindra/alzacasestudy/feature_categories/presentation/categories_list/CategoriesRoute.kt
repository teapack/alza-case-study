package cz.vratislavjindra.alzacasestudy.feature_categories.presentation.categories_list

import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import cz.vratislavjindra.alzacasestudy.R
import cz.vratislavjindra.alzacasestudy.core.utils.isScrolled
import cz.vratislavjindra.alzacasestudy.ui.components.error.ErrorFullscreen
import cz.vratislavjindra.alzacasestudy.ui.components.layout.AlzaScaffold

@Composable
fun CategoriesRoute(
    viewModel: CategoriesListViewModel = hiltViewModel(),
    onCategoryClick: (Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val categoriesLazyGridState = rememberLazyGridState()
    AlzaScaffold(
        modifier = Modifier,
        snackbarHostState = snackbarHostState,
        topBarTitle = stringResource(id = R.string.title_categories),
        upNavigationAction = null,
        contentIsScrolled = categoriesLazyGridState.isScrolled,
        content = { paddingValues ->
            uiState.errorMessage.let { errorMessage ->
                if (errorMessage != null) {
                    ErrorFullscreen(
                        paddingValues = paddingValues,
                        errorMessage = errorMessage,
                        onTryAgainButtonClick = viewModel::refreshCategories
                    )
                } else {
                    CategoriesScreenContent(
                        paddingValues = paddingValues,
                        categoriesLazyGridState = categoriesLazyGridState,
                        categories = uiState.categories,
                        loading = uiState.loading,
                        onRefresh = viewModel::refreshCategories,
                        onCategoryClick = { onCategoryClick(it) }
                    )
                }
            }
        }
    )
}