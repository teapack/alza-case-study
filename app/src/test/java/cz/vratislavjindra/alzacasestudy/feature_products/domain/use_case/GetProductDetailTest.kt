package cz.vratislavjindra.alzacasestudy.feature_products.domain.use_case

import android.content.Context
import cz.vratislavjindra.alzacasestudy.core.util.AlzaError
import cz.vratislavjindra.alzacasestudy.core.util.Resource
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
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock

@RunWith(MockitoJUnitRunner::class)
class GetProductDetailTest {

    private lateinit var getProductDetail: GetProductDetail
    private lateinit var fakeProductRepository: FakeProductRepository

    @Before
    fun setUp() {
        fakeProductRepository = FakeProductRepository()
        val mockContext = mock<Context> {
            on { getString(AlzaError.NO_SELECTED_PRODUCT.errorMessageResId) } doReturn "Invalid product"
        }
        getProductDetail = GetProductDetail(
            context = mockContext,
            productRepository = fakeProductRepository
        )
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
                        assertEquals(0, result.data?.id)
                        assertEquals("a", result.data?.name)
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
                        assertEquals(AlzaError.INVALID_PRODUCT, result.error)
                        assertEquals("Invalid product", result.errorMessage)
                        assertEquals(null, result.data?.id)
                    }
                    is Resource.Loading -> {
                    }
                }
            }
            .launchIn(scope = this)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `Get product with null ID`() = runTest {
        val getProductDetailResult = getProductDetail(productId = null)
        getProductDetailResult
            .onEach { result ->
                when (result) {
                    is Resource.Success -> {
                    }
                    is Resource.Error -> {
                        assertNull(result.data)
                        assertEquals(AlzaError.NO_SELECTED_PRODUCT, result.error)
                        assertEquals("Invalid product", result.errorMessage)
                        assertNull(result.data?.id)
                    }
                    is Resource.Loading -> {
                    }
                }
            }
            .launchIn(scope = this)
    }
}