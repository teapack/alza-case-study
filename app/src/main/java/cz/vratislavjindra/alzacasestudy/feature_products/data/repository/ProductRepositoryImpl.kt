package cz.vratislavjindra.alzacasestudy.feature_products.data.repository

import cz.vratislavjindra.alzacasestudy.R
import cz.vratislavjindra.alzacasestudy.core.data.Resource
import cz.vratislavjindra.alzacasestudy.core.utils.ErrorMessage
import cz.vratislavjindra.alzacasestudy.feature_products.data.ProductDataSource
import cz.vratislavjindra.alzacasestudy.feature_products.domain.model.Product
import cz.vratislavjindra.alzacasestudy.feature_products.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val productLocalDataSource: ProductDataSource,
    private val productRemoteDataSource: ProductDataSource
) : ProductRepository {

    override suspend fun getProductsByCategory(
        categoryId: Int
    ): Flow<Resource<List<Product>>> = flow {
        emit(value = Resource.Loading())
        val productsResult = productLocalDataSource.getProductsByCategory(categoryId = categoryId)
        emit(value = Resource.Loading(data = productsResult.getOrNull()))
        try {
            val responseResult = productRemoteDataSource.getProductsByCategory(
                categoryId = categoryId
            )
            if (responseResult.isSuccess) {
                val newProducts = responseResult.getOrThrow()
                // Clear old cached products.
                productLocalDataSource.deleteProductsByCategory(categoryId = categoryId)
                // Save the new products to cache.
                productLocalDataSource.saveProducts(products = newProducts)
            } else {
                // API response contained errors.
                throw responseResult.exceptionOrNull()
                    ?: IllegalStateException("API response contained errors.")
            }
            val newProductsResult = productLocalDataSource.getProductsByCategory(
                categoryId = categoryId
            )
            if (newProductsResult.isSuccess) {
                emit(value = Resource.Success(data = newProductsResult.getOrThrow()))
            } else {
                throw newProductsResult.exceptionOrNull()
                    ?: IllegalStateException("No products available.")
            }
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
        val productResult = productLocalDataSource.getProductDetail(id = id)
        emit(value = Resource.Loading(data = productResult.getOrNull()))
        try {
            val responseResult = productRemoteDataSource.getProductDetail(id = id)
            if (responseResult.isSuccess) {
                val newProduct = responseResult.getOrThrow()
                // Clear old cached product.
                productLocalDataSource.deleteProduct(id = id)
                // Save the new product to cache.
                productLocalDataSource.saveProduct(product = newProduct)
            } else {
                // API response contained errors.
                throw responseResult.exceptionOrNull()
                    ?: IllegalStateException("API response contained errors.")
            }
            val newProductResult = productLocalDataSource.getProductDetail(id = id)
            if (newProductResult.isSuccess) {
                emit(value = Resource.Success(data = newProductResult.getOrThrow()))
            } else {
                throw newProductResult.exceptionOrNull()
                    ?: IllegalArgumentException("Unable to find product.")
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