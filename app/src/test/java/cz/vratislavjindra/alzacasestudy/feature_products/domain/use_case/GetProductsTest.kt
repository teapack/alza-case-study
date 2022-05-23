package cz.vratislavjindra.alzacasestudy.feature_products.domain.use_case

import cz.vratislavjindra.alzacasestudy.feature_products.data.repository.FakeProductRepository
import cz.vratislavjindra.alzacasestudy.feature_products.domain.model.Product
import kotlinx.coroutines.runBlocking
import org.junit.Before

class GetProductsTest {

    private lateinit var getProducts: GetProducts
    private lateinit var fakeProductRepository: FakeProductRepository

    @Before
    fun setUp() {
        fakeProductRepository = FakeProductRepository()
        getProducts = GetProducts(productRepository = fakeProductRepository)
        val productsToInsert = mutableListOf<Product>()
        ('a'..'z').forEachIndexed { index, c ->
            productsToInsert.add(
                element = Product(
                    id = index,
                    name = c.toString(),
                    description = c.toString(),
                    imageUrl = null,
                    price = null,
                    availability = c.toString(),
                    canBuy = false,
                    rating = 0f
                )
            )
        }
        productsToInsert.shuffle()
        runBlocking {
            productsToInsert.forEach { fakeProductRepository.insertProduct(product = it) }
        }
    }
}