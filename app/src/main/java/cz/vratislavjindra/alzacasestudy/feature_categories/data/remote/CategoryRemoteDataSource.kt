package cz.vratislavjindra.alzacasestudy.feature_categories.data.remote

import cz.vratislavjindra.alzacasestudy.feature_categories.data.CategoryDataSource
import cz.vratislavjindra.alzacasestudy.feature_categories.domain.model.Category
import javax.inject.Inject

class CategoryRemoteDataSource @Inject constructor(
    private val api: CategoryApi
): CategoryDataSource {

    override suspend fun deleteAllCategories() {
        // NO-OP
    }

    override suspend fun getAllCategories(): Result<List<Category>> {
        return try {
            val response = api.getAllCategories()
            val data = response.getDataOrThrow().map { it.toCategory() }
            Result.success(value = data)
        } catch (exception: Exception) {
            Result.failure(exception = exception)
        }
    }

    override suspend fun saveCategories(categories: List<Category>) {
        // NO-OP
    }
}