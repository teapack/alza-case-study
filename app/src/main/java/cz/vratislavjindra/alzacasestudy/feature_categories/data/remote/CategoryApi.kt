package cz.vratislavjindra.alzacasestudy.feature_categories.data.remote

import cz.vratislavjindra.alzacasestudy.feature_categories.data.remote.dto.CategoryList
import retrofit2.http.GET

interface CategoryApi {

    @GET("v1/floors")
    suspend fun getAllCategories(): CategoryList
}