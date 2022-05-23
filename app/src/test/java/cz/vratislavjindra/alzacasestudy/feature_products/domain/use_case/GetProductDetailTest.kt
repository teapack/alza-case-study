package cz.vratislavjindra.alzacasestudy.feature_products.domain.use_case

import cz.vratislavjindra.alzacasestudy.R
import cz.vratislavjindra.alzacasestudy.core.data.Resource
import cz.vratislavjindra.alzacasestudy.feature_products.data.repository.FakeProductRepository
import cz.vratislavjindra.alzacasestudy.feature_products.domain.model.Product
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class GetProductDetailTest {

    private lateinit var getProductDetail: GetProductDetail
    private lateinit var fakeProductRepository: FakeProductRepository

    @Before
    fun setUp() {
        fakeProductRepository = FakeProductRepository()
        getProductDetail = GetProductDetail(productRepository = fakeProductRepository)
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

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `Get product by ID 0`() = runTest {
        val getProductDetailResult = getProductDetail(productId = 0)
        getProductDetailResult
            .onEach { result ->
                when (result) {
                    is Resource.Success -> {
                        assertNotNull(result.data)
                        assertEquals(0, result.data.id)
                        assertEquals("a", result.data.name)
                    }
                    is Resource.Error -> {
                    }
                    is Resource.Loading -> {
                    }
                }
            }
            .launchIn(scope = this)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `Get product with non-existent ID`() = runTest {
        val getProductDetailResult = getProductDetail(productId = -1)
        getProductDetailResult
            .onEach { result ->
                when (result) {
                    is Resource.Success -> {
                    }
                    is Resource.Error -> {
                        assertNull(result.data)
                        assertEquals(
                            R.string.error_load_product_detail,
                            result.errorMessage.messageId
                        )
                        assertEquals(null, result.data?.id)
                    }
                    is Resource.Loading -> {
                    }
                }
            }
            .launchIn(scope = this)
    }
}