package cz.vratislavjindra.alzacasestudy.feature_categories.data.remote.dto

import cz.vratislavjindra.alzacasestudy.feature_categories.domain.model.Category
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CategoryDto(
    val id: Int,
    val name: String,
    @SerialName("img") val imageUrl: String?,
    val order: Int
) {

    fun toCategory(): Category {
        return Category(
            id = id,
            name = name,
            imageUrl = imageUrl,
            order = order
        )
    }
}