package cz.vratislavjindra.alzacasestudy.feature_products.data.remote

import cz.vratislavjindra.alzacasestudy.feature_products.data.ProductDataSource
import cz.vratislavjindra.alzacasestudy.feature_products.data.remote.dao.FilterParameters
import cz.vratislavjindra.alzacasestudy.feature_products.data.remote.dao.GetProductsByCategoryRequest
import cz.vratislavjindra.alzacasestudy.feature_products.domain.model.Product
import javax.inject.Inject

class ProductRemoteDataSource @Inject constructor(private val api: ProductApi): ProductDataSource {

    override suspend fun deleteProduct(id: Int) {
        // NO-OP
    }

    override suspend fun deleteProductsByCategory(categoryId: Int) {
        // NO-OP
    }

    override suspend fun getProductDetail(id: Int): Result<Product> {
        return try {
            val response = api.getProductDetail(productId = id)
            val data = response.getDataOrThrow().toProduct()
            Result.success(value = data)
        } catch (exception: Exception) {
            Result.failure(exception = exception)
        }
    }

    override suspend fun getProductsByCategory(categoryId: Int): Result<List<Product>> {
        return try {
            val response = api.getProductsByCategory(
                requestBody = GetProductsByCategoryRequest(
                    filterParameters = FilterParameters(
                        id = categoryId,
                        params = emptyList()
                    )
                )
            )
            val data = response.getDataOrThrow().map { it.toProduct(categoryId = categoryId) }
            Result.success(value = data)
        } catch (exception: Exception) {
            Result.failure(exception = exception)
        }
    }

    override suspend fun saveProduct(product: Product) {
        // NO-OP
    }

    override suspend fun saveProducts(products: List<Product>) {
        // NO-OP
    }
}