package cz.vratislavjindra.alzacasestudy.feature_products.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class GetProductDetailResponse(
    val data: ProductDto
)