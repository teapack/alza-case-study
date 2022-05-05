package cz.vratislavjindra.alzacasestudy.feature_products.data.remote

import cz.vratislavjindra.alzacasestudy.feature_products.data.remote.dto.GetCategoriesResponse
import retrofit2.http.GET

interface CategoryApi {

    @GET("v1/floors")
    suspend fun getAllCategories(): GetCategoriesResponse
}