package cz.vratislavjindra.alzacasestudy.feature_products.presentation.product_detail

import cz.vratislavjindra.alzacasestudy.feature_products.domain.model.Product

data class ProductDetailScreenState(
    val loading: Boolean = true,
    val product: Product? = null
)