package cz.vratislavjindra.alzacasestudy.feature_products.data.local

import cz.vratislavjindra.alzacasestudy.feature_products.data.ProductDataSource
import cz.vratislavjindra.alzacasestudy.feature_products.data.local.entity.ProductEntity
import cz.vratislavjindra.alzacasestudy.feature_products.domain.model.Product
import javax.inject.Inject

class ProductLocalDataSource @Inject constructor(private val dao: ProductDao) : ProductDataSource {

    override suspend fun deleteProduct(id: Int) {
        dao.deleteProductById(id = id)
    }

    override suspend fun deleteProductsByCategory(categoryId: Int) {
        dao.deleteProductsByCategory(categoryId = categoryId)
    }

    override suspend fun getProductDetail(id: Int): Result<Product> {
        val product = dao.getProduct(id = id)?.toProduct()
        return if (product != null) {
            Result.success(value = product)
        } else {
            Result.failure(
                exception = IllegalArgumentException("Product with the given ID not found.")
            )
        }
    }

    override suspend fun getProductsByCategory(categoryId: Int): Result<List<Product>> {
        return Result.success(
            value = dao.getProductsByCategory(categoryId = categoryId).map { it.toProduct() }
        )
    }

    override suspend fun saveProduct(product: Product) {
        dao.insertProduct(product = ProductEntity(product = product))
    }

    override suspend fun saveProducts(products: List<Product>) {
        dao.insertProducts(products = products.map { ProductEntity(product = it) })
    }
}