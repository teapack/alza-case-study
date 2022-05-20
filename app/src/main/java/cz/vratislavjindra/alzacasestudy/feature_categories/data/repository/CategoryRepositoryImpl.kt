package cz.vratislavjindra.alzacasestudy.feature_categories.data.repository

import cz.vratislavjindra.alzacasestudy.R
import cz.vratislavjindra.alzacasestudy.core.data.Resource
import cz.vratislavjindra.alzacasestudy.core.utils.ErrorMessage
import cz.vratislavjindra.alzacasestudy.feature_categories.data.local.CategoryDao
import cz.vratislavjindra.alzacasestudy.feature_categories.data.remote.CategoryApi
import cz.vratislavjindra.alzacasestudy.feature_categories.domain.model.Category
import cz.vratislavjindra.alzacasestudy.feature_categories.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val api: CategoryApi,
    private val dao: CategoryDao
) : CategoryRepository {

    override suspend fun getAllCategories(): Flow<Resource<List<Category>>> = flow {
        emit(value = Resource.Loading())
        val categories = dao.getAllCategories().map { it.toCategory() }
        emit(value = Resource.Loading(data = categories))
        try {
            val response = api.getAllCategories()
            if (response.errorCode == 0 && response.errorMessage == null && response.data != null) {
                // Clear old cached categories.
                dao.deleteAllCategories()
                // Save the new categories to cache.
                dao.insertCategories(categories = response.data.map { it.toCategoryEntity() })
            } else {
                // API response contained errors.
                throw IllegalStateException("API response contained errors.")
            }
            val newCategories = dao.getAllCategories().map { it.toCategory() }
            emit(value = Resource.Success(data = newCategories))
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