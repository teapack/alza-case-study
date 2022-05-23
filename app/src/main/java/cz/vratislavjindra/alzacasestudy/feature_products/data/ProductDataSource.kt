package cz.vratislavjindra.alzacasestudy.feature_products.data

import cz.vratislavjindra.alzacasestudy.feature_products.domain.model.Product

interface ProductDataSource {

    suspend fun deleteProduct(id: Int)

    suspend fun deleteProductsByCategory(categoryId: Int)

    suspend fun getProductDetail(id: Int): Result<Product>

    suspend fun getProductsByCategory(categoryId: Int): Result<List<Product>>

    suspend fun saveProduct(product: Product)

    suspend fun saveProducts(products: List<Product>)
}