package cz.vratislavjindra.alzacasestudy.feature_products.domain.use_case

import cz.vratislavjindra.alzacasestudy.R
import cz.vratislavjindra.alzacasestudy.core.data.Resource
import cz.vratislavjindra.alzacasestudy.feature_products.data.repository.EXISTING_CATEGORY_ID
import cz.vratislavjindra.alzacasestudy.feature_products.data.repository.FakeProductRepository
import cz.vratislavjindra.alzacasestudy.feature_products.data.repository.NON_EXISTENT_CATEGORY_ID
import cz.vratislavjindra.alzacasestudy.feature_products.domain.model.Product
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

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
                    rating = 0f,
                    order = index,
                    categoryId = EXISTING_CATEGORY_ID
                )
            )
        }
        productsToInsert.shuffle()
        runBlocking {
            productsToInsert.forEach { fakeProductRepository.insertProduct(product = it) }
        }
    }

    @Test
    fun `Get products by existing category ID`() = runBlocking {
        val getProductsResult = getProducts(categoryId = EXISTING_CATEGORY_ID)
        val resultList = getProductsResult.toList()
        assertNotNull(resultList)
        assertTrue(resultList.size == 3)
        assertTrue(resultList[0] is Resource.Loading)
        assertNull((resultList[0] as Resource.Loading).data)
        assertTrue(resultList[1] is Resource.Loading)
        assertNotNull((resultList[1] as Resource.Loading).data)
        assertTrue(resultList[2] is Resource.Success)
        assertNotNull((resultList[2] as Resource.Success).data)
        assertTrue((resultList[2] as Resource.Success).data.isNotEmpty())
        assertEquals(
            26,
            (resultList[2] as Resource.Success).data.size
        )
    }

    @Test
    fun `Get products by non-existent category ID`() = runBlocking {
        val getProductsResult = getProducts(categoryId = NON_EXISTENT_CATEGORY_ID)
        val resultList = getProductsResult.toList()
        assertNotNull(resultList)
        assertTrue(resultList.size == 3)
        assertTrue(resultList[0] is Resource.Loading)
        assertNull((resultList[0] as Resource.Loading).data)
        assertTrue(resultList[1] is Resource.Loading)
        assertNull((resultList[1] as Resource.Loading).data)
        assertTrue(resultList[2] is Resource.Error)
        assertEquals(
            R.string.error_load_products,
            (resultList[2] as Resource.Error).errorMessage.messageId
        )
    }
}