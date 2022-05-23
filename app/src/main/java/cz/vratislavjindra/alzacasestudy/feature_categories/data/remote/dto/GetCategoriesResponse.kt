package cz.vratislavjindra.alzacasestudy.feature_categories.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetCategoriesResponse(
    val data: List<CategoryDto>?,
    @SerialName("err") val errorCode: Int,
    @SerialName("msg") val errorMessage: String?
)