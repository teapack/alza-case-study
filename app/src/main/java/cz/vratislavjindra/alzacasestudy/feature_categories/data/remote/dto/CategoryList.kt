package cz.vratislavjindra.alzacasestudy.feature_categories.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class CategoryList(
    val data: List<CategoryDto>
)