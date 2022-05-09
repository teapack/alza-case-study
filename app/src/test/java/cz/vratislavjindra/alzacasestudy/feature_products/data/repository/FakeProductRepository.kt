package cz.vratislavjindra.alzacasestudy.feature_products.data.repository

import cz.vratislavjindra.alzacasestudy.core.util.AlzaError
import cz.vratislavjindra.alzacasestudy.core.util.Resource
import cz.vratislavjindra.alzacasestudy.feature_products.domain.model.Product
import cz.vratislavjindra.alzacasestudy.feature_products.domain.model.ProductListItem
import cz.vratislavjindra.alzacasestudy.feature_products.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeProductRepository : ProductRepository {

    private val products = mutableListOf<Product>()

    override suspend fun getProductsByCategory(
        categoryId: Int
    ): Flow<Resource<List<ProductListItem>>> {
        TODO("Not yet implemented")
    }

    override suspend fun getProductDetail(id: Int): Flow<Resource<Product>> {
        return flow {
            val product = products.find { it.id == id }
            if (product == null) {
                emit(
                    value = Resource.Error(
                        error = AlzaError.INVALID_PRODUCT,
                        errorMessage = "Invalid product",
                        data = null
                    )
                )
            } else {
                emit(value = Resource.Success(data = product))
            }
        }
    }

    fun insertProduct(product: Product) {
        products.add(element = product)
    }
}