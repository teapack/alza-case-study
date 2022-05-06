package cz.vratislavjindra.alzacasestudy.feature_products.presentation.products

import cz.vratislavjindra.alzacasestudy.feature_products.domain.model.ProductListItem

data class ProductsScreenState(
    val loading: Boolean = true,
    val products: List<ProductListItem> = emptyList()
)