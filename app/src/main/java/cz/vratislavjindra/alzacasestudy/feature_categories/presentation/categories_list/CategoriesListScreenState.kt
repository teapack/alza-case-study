package cz.vratislavjindra.alzacasestudy.feature_categories.presentation.categories_list

import cz.vratislavjindra.alzacasestudy.feature_categories.domain.model.Category

data class CategoriesListScreenState(
    val loading: Boolean = true,
    val categories: List<Category> = emptyList()
)
