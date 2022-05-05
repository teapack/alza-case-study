package cz.vratislavjindra.alzacasestudy.feature_products.domain.repository

import cz.vratislavjindra.alzacasestudy.core.util.Resource
import cz.vratislavjindra.alzacasestudy.feature_products.domain.model.Product
import cz.vratislavjindra.alzacasestudy.feature_products.domain.model.ProductOverview
import kotlinx.coroutines.flow.Flow

interface ProductRepository {

    suspend fun getProductsByCategory(categoryId: Int): Flow<Resource<List<ProductOverview>>>

    suspend fun getProductDetail(id: Int): Flow<Resource<Product>>
}