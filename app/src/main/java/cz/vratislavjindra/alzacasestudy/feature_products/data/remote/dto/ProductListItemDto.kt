package cz.vratislavjindra.alzacasestudy.feature_products.data.remote.dto

import cz.vratislavjindra.alzacasestudy.feature_products.domain.model.Product
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductListItemDto(
    val id: Int,
    val name: String,
    @SerialName("spec") val description: String,
    @SerialName("img") val imageUrl: String?,
    val price: String?,
    @SerialName("avail") val availability: String,
    @SerialName("can_buy") val canBuy: Boolean,
    val rating: Float,
    val order: Int
) {

    fun toProduct(categoryId: Int): Product {
        return Product(
            id = id,
            name = name,
            description = description,
            imageUrl = imageUrl,
            price = price,
            availability = availability,
            canBuy = canBuy,
            rating = rating,
            order = order,
            categoryId = categoryId
        )
    }
}