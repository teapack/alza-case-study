package cz.vratislavjindra.alzacasestudy.feature_products.data.repository

import android.content.Context
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import cz.vratislavjindra.alzacasestudy.core.util.AlzaError
import cz.vratislavjindra.alzacasestudy.core.util.Resource
import cz.vratislavjindra.alzacasestudy.feature_products.data.local.CategoryDao
import cz.vratislavjindra.alzacasestudy.feature_products.data.remote.CategoryApi
import cz.vratislavjindra.alzacasestudy.feature_products.domain.model.Category
import cz.vratislavjindra.alzacasestudy.feature_products.domain.repository.CategoryRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val api: CategoryApi,
    private val dao: CategoryDao
) : CategoryRepository {

    override suspend fun getAllCategories(): Flow<Resource<List<Category>>> = flow {
        emit(value = Resource.Loading())
        val categories = dao.getAllCategories().map { it.toCategory() }
        emit(value = Resource.Loading(data = categories))
        var errorOccurred = false
        try {
            val categoryList = api.getAllCategories()
            // Clear old cached categories.
            dao.deleteAllCategories()
            // Save the new categories to cache.
            dao.insertCategories(categories = categoryList.data.map { it.toCategoryEntity() })
            // Schedule a worker to clear the cache after 1 minute.
            val clearCacheWorkRequest = OneTimeWorkRequestBuilder<ClearCategoriesCacheWorker>()
                    .setInitialDelay(1, TimeUnit.MINUTES)
                    .addTag(CLEAR_CATEGORIES_CACHE_WORKER_TAG)
                    .build()
            WorkManager.getInstance(context).apply {
                cancelAllWorkByTag(CLEAR_CATEGORIES_CACHE_WORKER_TAG)
                enqueue(clearCacheWorkRequest)
            }
        } catch (e: HttpException) {
            // TODO I should handle all the exceptions better (I'm duplicating code here).
            errorOccurred = true
            e.printStackTrace()
            emit(value = Resource.Error(error = AlzaError.HTTP_ERROR, errorMessage = e.message()))
        } catch (e: IOException) {
            // We're most likely offline.
            errorOccurred = true
            e.printStackTrace()
            val error = AlzaError.HTTP_ERROR
            emit(
                value = Resource.Error(
                    error = error,
                    errorMessage = e.message ?: context.getString(error.errorMessageResId)
                )
            )
        } catch (e: SocketTimeoutException) {
            errorOccurred = true
            e.printStackTrace()
            val error = AlzaError.TIMEOUT
            emit(
                value = Resource.Error(
                    error = error,
                    errorMessage = context.getString(error.errorMessageResId)
                )
            )
        }
        if (!errorOccurred) {
            val newCategories = dao.getAllCategories().map { it.toCategory() }
            emit(value = Resource.Success(data = newCategories))
        }
    }
}