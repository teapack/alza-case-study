package cz.vratislavjindra.alzacasestudy.feature_products.data.remote.dto

import cz.vratislavjindra.alzacasestudy.core.data.remote.BaseResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetProductsResponse(
    override val data: List<ProductListItemDto>?,
    @SerialName("err") override val errorCode: Int,
    @SerialName("msg") override val errorMessage: String?
): BaseResponse<List<ProductListItemDto>>()