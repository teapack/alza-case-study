package cz.vratislavjindra.alzacasestudy.feature_products.data.remote.dto

import cz.vratislavjindra.alzacasestudy.core.data.remote.BaseResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetProductDetailResponse(
    override val data: ProductDto?,
    @SerialName("err") override val errorCode: Int,
    @SerialName("msg") override val errorMessage: String?
): BaseResponse<ProductDto>()