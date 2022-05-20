package cz.vratislavjindra.alzacasestudy.feature_products.domain.use_case

import cz.vratislavjindra.alzacasestudy.core.data.Resource
import cz.vratislavjindra.alzacasestudy.feature_products.domain.model.Product
import cz.vratislavjindra.alzacasestudy.feature_products.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProducts @Inject constructor(private val productRepository: ProductRepository) {

    suspend operator fun invoke(categoryId: Int): Flow<Resource<List<Product>>> {
        return productRepository.getProductsByCategory(categoryId = categoryId)
    }
}