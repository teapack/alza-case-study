package cz.vratislavjindra.alzacasestudy.feature_products.domain.model

data class Product(
    val id: Int,
    val name: String,
    val description: String,
    val imageUrl: String?
)