package cz.vratislavjindra.alzacasestudy.feature_products.data.repository

import cz.vratislavjindra.alzacasestudy.R
import cz.vratislavjindra.alzacasestudy.core.data.Resource
import cz.vratislavjindra.alzacasestudy.core.utils.ErrorMessage
import cz.vratislavjindra.alzacasestudy.feature_products.domain.model.Product
import cz.vratislavjindra.alzacasestudy.feature_products.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

const val EXISTING_CATEGORY_ID = 0
const val NON_EXISTENT_CATEGORY_ID = -1

class FakeProductRepository : ProductRepository {

    private val products = mutableListOf<Product>()

    override suspend fun getProductsByCategory(categoryId: Int): Flow<Resource<List<Product>>> {
        return flow {
            emit(value = Resource.Loading())
            if (categoryId == EXISTING_CATEGORY_ID) {
                val products = products.filter { it.categoryId == categoryId }
                emit(value = Resource.Loading(data = products))
                emit(value = Resource.Success(data = products))
            } else {
                emit(value = Resource.Loading(data = null))
                emit(
                    value = Resource.Error(
                        errorMessage = ErrorMessage(
                            messageId = R.string.error_load_products
                        )
                    )
                )
            }
        }
    }

    override suspend fun getProductDetail(id: Int): Flow<Resource<Product>> {
        return flow {
            val product = products.find { it.id == id }
            if (product == null) {
                emit(
                    value = Resource.Error(
                        errorMessage = ErrorMessage(
                            messageId = R.string.error_load_product_detail
                        )
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