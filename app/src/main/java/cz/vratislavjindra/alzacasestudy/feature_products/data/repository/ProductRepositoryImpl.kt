package cz.vratislavjindra.alzacasestudy.feature_products.data.repository

import cz.vratislavjindra.alzacasestudy.R
import cz.vratislavjindra.alzacasestudy.core.data.Resource
import cz.vratislavjindra.alzacasestudy.core.utils.ErrorMessage
import cz.vratislavjindra.alzacasestudy.feature_products.data.local.ProductDao
import cz.vratislavjindra.alzacasestudy.feature_products.data.remote.ProductApi
import cz.vratislavjindra.alzacasestudy.feature_products.data.remote.dao.FilterParameters
import cz.vratislavjindra.alzacasestudy.feature_products.data.remote.dao.GetProductsByCategoryRequest
import cz.vratislavjindra.alzacasestudy.feature_products.domain.model.Product
import cz.vratislavjindra.alzacasestudy.feature_products.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val dao: ProductDao,
    private val api: ProductApi
) : ProductRepository {

    override suspend fun getProductsByCategory(
        categoryId: Int
    ): Flow<Resource<List<Product>>> = flow {
        emit(value = Resource.Loading())
        val products = dao.getProductsByCategory(categoryId = categoryId).map { it.toProduct() }
        emit(value = Resource.Loading(data = products))
        try {
            val response = api.getProductsByCategory(
                requestBody = GetProductsByCategoryRequest(
                    filterParameters = FilterParameters(
                        id = categoryId,
                        params = emptyList()
                    )
                )
            )
            if (
                response.errorCode == 0 &&
                response.errorMessage == null &&
                response.data != null
            ) {
                // Clear old cached products.
                dao.deleteProductsByCategory(categoryId = categoryId)
                // Save the new products to cache.
                dao.insertProducts(
                    products = response.data.map { it.toProductEntity(categoryId = categoryId) }
                )
            } else {
                // API response contained errors.
                throw IllegalStateException("API response contained errors.")
            }
            val newProducts = dao.getProductsByCategory(categoryId = categoryId).map {
                it.toProduct()
            }
            emit(value = Resource.Success(data = newProducts))
        } catch (exception: Exception) {
            exception.printStackTrace()
            emit(
                value = Resource.Error(
                    errorMessage = ErrorMessage(
                        messageId = R.string.error_load_products
                    )
                )
            )
        }
    }

    override suspend fun getProductDetail(id: Int): Flow<Resource<Product>> = flow {
        emit(value = Resource.Loading())
        val product = dao.getProduct(id = id)?.toProduct()
        emit(value = Resource.Loading(data = product))
        try {
            val response = api.getProductDetail(productId = id)
            if (
                response.errorCode == 0 &&
                response.errorMessage == null &&
                response.data != null
            ) {
                // Clear old cached product.
                dao.deleteProductById(id = id)
                // Save the new product to cache.
                dao.insertProduct(product = response.data.toProductEntity())
            } else {
                // API response contained errors.
                throw IllegalStateException("API response contained errors.")
            }
            val newProduct = dao.getProduct(id = id)?.toProduct()
            if (newProduct == null) {
                throw IllegalArgumentException("Unable to find product.")
            } else {
                emit(value = Resource.Success(data = newProduct))
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
            emit(
                value = Resource.Error(
                    errorMessage = ErrorMessage(
                        messageId = R.string.error_load_product_detail
                    )
                )
            )
        }
    }
}