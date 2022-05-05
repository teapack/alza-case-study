package cz.vratislavjindra.alzacasestudy.feature_products.data.remote

import cz.vratislavjindra.alzacasestudy.feature_products.data.remote.dao.GetProductsByCategoryRequest
import cz.vratislavjindra.alzacasestudy.feature_products.data.remote.dto.ProductDto
import cz.vratislavjindra.alzacasestudy.feature_products.data.remote.dto.GetProductsResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ProductApi {

    @POST("v2/products")
    suspend fun getProductsByCategory(@Body requestBody: GetProductsByCategoryRequest): GetProductsResponse

    @GET("v13/product/{productId}")
    suspend fun getProductDetail(@Path("productId") productId: Int): ProductDto
}