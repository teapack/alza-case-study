package cz.vratislavjindra.alzacasestudy.feature_categories.data.remote.dto

import cz.vratislavjindra.alzacasestudy.core.data.remote.BaseResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetCategoriesResponse(
    override val data: List<CategoryDto>?,
    @SerialName("err") override val errorCode: Int,
    @SerialName("msg") override val errorMessage: String?
): BaseResponse<List<CategoryDto>>()