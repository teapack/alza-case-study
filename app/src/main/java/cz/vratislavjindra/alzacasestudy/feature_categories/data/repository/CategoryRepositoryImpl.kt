package cz.vratislavjindra.alzacasestudy.feature_categories.data.repository

import cz.vratislavjindra.alzacasestudy.core.util.AlzaError
import cz.vratislavjindra.alzacasestudy.core.util.Resource
import cz.vratislavjindra.alzacasestudy.feature_categories.data.local.CategoryDao
import cz.vratislavjindra.alzacasestudy.feature_categories.data.remote.CategoryApi
import cz.vratislavjindra.alzacasestudy.feature_categories.domain.model.Category
import cz.vratislavjindra.alzacasestudy.feature_categories.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
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
            val categoryList = api.getAllCategories()
            dao.deleteAllCategories()
            dao.insertCategories(categories = categoryList.data.map { it.toCategoryEntity() })
        } catch (e: HttpException) {
            // TODO Properly handle the exception.
            e.printStackTrace()
            emit(value = Resource.Error(error = AlzaError.HTTP_ERROR, errorMessage = e.message()))
        } catch (e: IOException) {
            // TODO We're most likely offline. Properly handle the exception.
            e.printStackTrace()
            emit(
                value = Resource.Error(
                    error = AlzaError.HTTP_ERROR,
                    errorMessage = e.message ?: e.toString()
                )
            )
        }
        // TODO We should only execute this code when we get the data successfully.
        val newCategories = dao.getAllCategories().map { it.toCategory() }
        emit(value = Resource.Success(data = newCategories))
    }
}