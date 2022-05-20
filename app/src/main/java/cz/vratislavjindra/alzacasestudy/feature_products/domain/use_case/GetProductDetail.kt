package cz.vratislavjindra.alzacasestudy.feature_products.domain.use_case

import cz.vratislavjindra.alzacasestudy.core.data.Resource
import cz.vratislavjindra.alzacasestudy.feature_products.domain.model.Product
import cz.vratislavjindra.alzacasestudy.feature_products.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProductDetail @Inject constructor(private val productRepository: ProductRepository) {

    suspend operator fun invoke(productId: Int): Flow<Resource<Product>> {
        return productRepository.getProductDetail(id = productId)
    }
}