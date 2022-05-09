package cz.vratislavjindra.alzacasestudy.feature_products.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetProductDetailResponse(
    val data: ProductDto?,
    @SerialName("err") val errorCode: Int,
    @SerialName("msg") val errorMessage: String?
)