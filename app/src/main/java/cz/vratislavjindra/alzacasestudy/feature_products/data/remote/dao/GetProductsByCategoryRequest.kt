package cz.vratislavjindra.alzacasestudy.feature_products.data.remote.dao

import kotlinx.serialization.Serializable

@Serializable
data class GetProductsByCategoryRequest(
    val filterParameters: FilterParameters
)