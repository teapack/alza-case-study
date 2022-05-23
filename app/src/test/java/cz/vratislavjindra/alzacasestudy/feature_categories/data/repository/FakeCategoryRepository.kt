package cz.vratislavjindra.alzacasestudy.feature_categories.data.repository

import cz.vratislavjindra.alzacasestudy.R
import cz.vratislavjindra.alzacasestudy.core.data.Resource
import cz.vratislavjindra.alzacasestudy.core.utils.ErrorMessage
import cz.vratislavjindra.alzacasestudy.feature_categories.domain.model.Category
import cz.vratislavjindra.alzacasestudy.feature_categories.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeCategoryRepository : CategoryRepository {

    private val categories = mutableListOf<Category>()
    private val cachedCategories = mutableListOf<Category>()
    var returnError = false

    override suspend fun getAllCategories(): Flow<Resource<List<Category>>> {
        return flow {
            emit(value = Resource.Loading())
            emit(value = Resource.Loading(data = cachedCategories))
            if (returnError) {
                emit(
                    value = Resource.Error(
                        errorMessage = ErrorMessage(
                            messageId = R.string.error_load_categories
                        )
                    )
                )
            } else {
                emit(value = Resource.Success(data = categories))
            }
        }
    }

    fun clearCategories() {
        categories.clear()
    }

    fun insertCategory(category: Category) {
        categories.add(element = category)
    }
}