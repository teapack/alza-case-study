package cz.vratislavjindra.alzacasestudy.feature_products.data.repository

import android.content.Context
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import cz.vratislavjindra.alzacasestudy.core.util.AlzaError
import cz.vratislavjindra.alzacasestudy.core.util.Resource
import cz.vratislavjindra.alzacasestudy.feature_products.data.local.ProductDao
import cz.vratislavjindra.alzacasestudy.feature_products.data.remote.ProductApi
import cz.vratislavjindra.alzacasestudy.feature_products.data.remote.dao.FilterParameters
import cz.vratislavjindra.alzacasestudy.feature_products.data.remote.dao.GetProductsByCategoryRequest
import cz.vratislavjindra.alzacasestudy.feature_products.domain.model.Product
import cz.vratislavjindra.alzacasestudy.feature_products.domain.model.ProductListItem
import cz.vratislavjindra.alzacasestudy.feature_products.domain.repository.ProductRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val dao: ProductDao,
    private val api: ProductApi
) : ProductRepository {

    override suspend fun getProductsByCategory(
        categoryId: Int
    ): Flow<Resource<List<ProductListItem>>> = flow {
        emit(value = Resource.Loading())
        val products = dao.getProductsByCategory(categoryId = categoryId).map {
            it.toProductListItem()
        }
        emit(value = Resource.Loading(data = products))
        var errorOccurred = false
        try {
            val response = api.getProductsByCategory(
                requestBody = GetProductsByCategoryRequest(
                    filterParameters = FilterParameters(
                        id = categoryId,
                        params = emptyList()
                    )
                )
            )
            if (response.errorCode == 0 && response.errorMessage == null && response.data != null) {
                // Clear old cached products.
                dao.deleteProductsByCategory(categoryId = categoryId)
                // Save the new products to cache.
                dao.insertProducts(
                    products = response.data.map { it.toProductEntity(categoryId = categoryId) }
                )
                // Schedule a worker to clear the cache after 1 minute.
                val clearCacheWorkRequest = OneTimeWorkRequestBuilder<ClearProductsCacheWorker>()
                    .setInitialDelay(1, TimeUnit.MINUTES)
                    .addTag(CLEAR_PRODUCTS_CACHE_WORKER_TAG)
                    .addTag("$CLEAR_PRODUCTS_CACHE_WORKER_TAG_PREFIX$categoryId")
                    .setInputData(
                        workDataOf(
                            Pair(
                                first = CLEAR_PRODUCTS_CACHE_INPUT_DATA_KEY_CATEGORY_ID,
                                second = categoryId
                            )
                        )
                    )
                    .build()
                WorkManager.getInstance(context).apply {
                    cancelAllWorkByTag("$CLEAR_PRODUCTS_CACHE_WORKER_TAG_PREFIX$categoryId")
                    enqueue(clearCacheWorkRequest)
                }
            } else {
                val error = AlzaError.HTTP_ERROR
                emit(
                    value = Resource.Error(
                        error = error,
                        errorMessage = response.errorMessage
                            ?: context.getString(error.errorMessageResId)
                    )
                )
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
            emit(
                value = Resource.Error(
                    error = AlzaError.HTTP_ERROR,
                    errorMessage = e.message ?: e.toString()
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
            val newProducts = dao.getProductsByCategory(categoryId = categoryId).map {
                it.toProductListItem()
            }
            emit(value = Resource.Success(data = newProducts))
        }
    }

    override suspend fun getProductDetail(id: Int): Flow<Resource<Product>> = flow {
        emit(value = Resource.Loading())
        val product = dao.getProduct(id = id).toProduct()
        emit(value = Resource.Loading(data = product))
        var errorOccurred = false
        try {
            val productDetailResponse = api.getProductDetail(productId = id)
            // Clear old cached product.
            dao.deleteProductById(id = id)
            // Save the new product to cache.
            dao.insertProduct(product = productDetailResponse.data.toProductEntity())
            // Schedule a worker to clear the cache after 1 minute.
            val clearCacheWorkRequest = OneTimeWorkRequestBuilder<ClearProductsCacheWorker>()
                .setInitialDelay(1, TimeUnit.MINUTES)
                .addTag(CLEAR_PRODUCT_CACHE_WORKER_TAG)
                .addTag("$CLEAR_PRODUCT_CACHE_WORKER_TAG_PREFIX$id")
                .setInputData(
                    workDataOf(
                        Pair(
                            first = CLEAR_PRODUCT_CACHE_INPUT_DATA_KEY_PRODUCT_ID,
                            second = id
                        )
                    )
                )
                .build()
            WorkManager.getInstance(context).apply {
                cancelAllWorkByTag("$CLEAR_PRODUCT_CACHE_WORKER_TAG_PREFIX$id")
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
            emit(
                value = Resource.Error(
                    error = AlzaError.HTTP_ERROR,
                    errorMessage = e.message ?: e.toString()
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
            val newProduct = dao.getProduct(id = id).toProduct()
            emit(value = Resource.Success(data = newProduct))
        }
    }
}