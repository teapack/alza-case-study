package cz.vratislavjindra.alzacasestudy.feature_products.domain.repository

import cz.vratislavjindra.alzacasestudy.core.data.Resource
import cz.vratislavjindra.alzacasestudy.feature_products.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {

    suspend fun getProductsByCategory(categoryId: Int): Flow<Resource<List<Product>>>

    suspend fun getProductDetail(id: Int): Flow<Resource<Product>>
}