package cz.vratislavjindra.alzacasestudy.feature_products.data.repository

import android.content.Context
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
                dao.deleteProductsByCategory(categoryId = categoryId)
                dao.insertProducts(
                    products = response.data.map { it.toProductEntity(categoryId = categoryId) }
                )
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
            dao.deleteProductById(id = id)
            dao.insertProduct(product = productDetailResponse.data.toProductEntity())
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