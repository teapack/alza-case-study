package cz.vratislavjindra.alzacasestudy.feature_categories.presentation.categories_list

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer
import cz.vratislavjindra.alzacasestudy.R
import cz.vratislavjindra.alzacasestudy.feature_categories.domain.model.Category
import cz.vratislavjindra.alzacasestudy.ui.components.card.SurfaceCard
import cz.vratislavjindra.alzacasestudy.ui.components.image.ItemImage
import cz.vratislavjindra.alzacasestudy.ui.components.layout.EmptyContentPlaceholder
import cz.vratislavjindra.alzacasestudy.ui.components.layout.SwipeRefreshLayout
import cz.vratislavjindra.alzacasestudy.ui.components.list.ListItemTitle
import cz.vratislavjindra.alzacasestudy.ui.components.loading.shimmerPlaceholder

@Composable
fun CategoriesScreenContent(
    paddingValues: PaddingValues,
    categoriesLazyGridState: LazyGridState,
    categories: List<Category>,
    loading: Boolean,
    onRefresh: () -> Unit,
    onCategoryClick: (categoryId: Int) -> Unit
) {
    SwipeRefreshLayout(
        isRefreshing = loading,
        onRefresh = onRefresh,
        paddingValues = paddingValues
    ) {
        if (!loading && categories.isEmpty()) {
            // We're not loading data anymore and no categories were returned. It's not an error,
            // though, it's just that the API doesn't respond with any data.
            EmptyContentPlaceholder(
                modifier = Modifier,
                paddingValues = paddingValues,
                text = stringResource(id = R.string.title_no_categories_available)
            )
        } else {
            val navigationBarsPadding = WindowInsets.navigationBars.only(
                sides = WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom
            ).asPaddingValues().calculateBottomPadding()
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 176.dp),
                modifier = Modifier.fillMaxSize(),
                state = categoriesLazyGridState,
                contentPadding = PaddingValues(
                    start = paddingValues.calculateStartPadding(
                        layoutDirection = LayoutDirection.Ltr
                    ) + 8.dp,
                    top = paddingValues.calculateTopPadding() + 8.dp,
                    end = paddingValues.calculateEndPadding(
                        layoutDirection = LayoutDirection.Rtl
                    ) + 8.dp,
                    bottom = navigationBarsPadding + 8.dp
                )
            ) {
                // TODO I have never done skeleton UIs before, this is my first shot at it. I guess it could be done better than passing dummy skeleton categories here.
                items(
                    items = categories.ifEmpty {
                        val skeletonCategories = mutableListOf<Category>()
                        for (i in 0..19) {
                            skeletonCategories.add(
                                element = Category(
                                    id = i,
                                    name = "",
                                    imageUrl = null,
                                    order = i
                                )
                            )
                        }
                        skeletonCategories.toList()
                    }
                ) { category ->
                    CategoryCard(
                        name = category.name,
                        imageUrl = category.imageUrl,
                        modifier = Modifier.padding(all = 8.dp),
                        loading = loading,
                        onClick = { onCategoryClick(category.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun CategoryCard(
    name: String,
    imageUrl: String?,
    modifier: Modifier = Modifier,
    loading: Boolean,
    onClick: () -> Unit
) {
    SurfaceCard(
        modifier = modifier
            .height(height = 80.dp)
            .shimmerPlaceholder(visible = loading),
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