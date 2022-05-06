package cz.vratislavjindra.alzacasestudy.feature_products.data.remote.dto

import cz.vratislavjindra.alzacasestudy.feature_products.data.local.entity.CategoryEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CategoryDto(
    val id: Int,
    val name: String,
    @SerialName("img") val imageUrl: String?,
    val order: Int
) {

    fun toCategoryEntity(): CategoryEntity {
        return CategoryEntity(
            id = id,
            name = name,
            imageUrl = imageUrl,
            order = order
        )
    }
}