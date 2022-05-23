package cz.vratislavjindra.alzacasestudy.feature_categories.data.repository

import cz.vratislavjindra.alzacasestudy.R
import cz.vratislavjindra.alzacasestudy.core.data.Resource
import cz.vratislavjindra.alzacasestudy.core.utils.ErrorMessage
import cz.vratislavjindra.alzacasestudy.feature_categories.data.CategoryDataSource
import cz.vratislavjindra.alzacasestudy.feature_categories.data.local.CategoryDao
import cz.vratislavjindra.alzacasestudy.feature_categories.data.remote.CategoryApi
import cz.vratislavjindra.alzacasestudy.feature_categories.domain.model.Category
import cz.vratislavjindra.alzacasestudy.feature_categories.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val categoryLocalDataSource: CategoryDataSource,
    private val categoryRemoteDataSource: CategoryDataSource
) : CategoryRepository {

    override suspend fun getAllCategories(): Flow<Resource<List<Category>>> = flow {
        emit(value = Resource.Loading())
        val categoriesResult = categoryLocalDataSource.getAllCategories()
        emit(value = Resource.Loading(data = categoriesResult.getOrNull()))
        try {
            val responseResult = categoryRemoteDataSource.getAllCategories()
            if (categoriesResult.isSuccess) {
                val newCategories = responseResult.getOrThrow()
                // Clear old cached categories.
                categoryLocalDataSource.deleteAllCategories()
                // Save the new categories to cache.
                categoryLocalDataSource.saveCategories(categories = newCategories)
            } else {
                // API response contained errors.
                throw responseResult.exceptionOrNull()
                    ?: IllegalStateException("API response contained errors.")
            }
            val newCategoriesResult = categoryLocalDataSource.getAllCategories()
            if (newCategoriesResult.isSuccess) {
                emit(value = Resource.Success(data = newCategoriesResult.getOrThrow()))
            } else {
                throw newCategoriesResult.exceptionOrNull()
                    ?: IllegalStateException("No categories available.")
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
            emit(
                value = Resource.Error(
                    errorMessage = ErrorMessage(
                        messageId = R.string.error_load_categories
                    )
                )
            )
        }
    }
}