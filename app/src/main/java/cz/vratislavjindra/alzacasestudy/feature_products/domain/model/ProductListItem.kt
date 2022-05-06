package cz.vratislavjindra.alzacasestudy.feature_products.domain.model

data class ProductListItem(
    val id: Int,
    val name: String,
    val description: String,
    val imageUrl: String?,
    val price: String?,
    val availability: String,
    val canBuy: Boolean,
    val rating: Float
)