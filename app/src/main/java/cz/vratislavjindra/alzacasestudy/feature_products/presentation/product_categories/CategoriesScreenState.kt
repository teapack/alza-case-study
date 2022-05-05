package cz.vratislavjindra.alzacasestudy.feature_products.presentation.product_categories

import cz.vratislavjindra.alzacasestudy.feature_products.domain.model.Category

data class CategoriesScreenState(
    val loading: Boolean = true,
    val categories: List<Category> = emptyList()
)