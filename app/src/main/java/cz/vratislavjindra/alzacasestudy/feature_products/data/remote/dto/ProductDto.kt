package cz.vratislavjindra.alzacasestudy.feature_products.data.remote.dto

import cz.vratislavjindra.alzacasestudy.feature_products.data.local.entity.ProductEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductDto(
    val id: Int,
    val name: String,
    @SerialName("spec") val description: String,
    @SerialName("img") val imageUrl: String?,
    val categoryId: Int
) {

    fun toProductEntity(): ProductEntity {
        return ProductEntity(
            id = id,
            name = name,
            description = description,
            imageUrl = imageUrl,
            categoryId = categoryId
        )
    }
}