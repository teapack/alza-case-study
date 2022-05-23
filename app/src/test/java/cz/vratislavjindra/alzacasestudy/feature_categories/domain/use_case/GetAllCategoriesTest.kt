package cz.vratislavjindra.alzacasestudy.feature_categories.domain.use_case

import cz.vratislavjindra.alzacasestudy.R
import cz.vratislavjindra.alzacasestudy.core.data.Resource
import cz.vratislavjindra.alzacasestudy.feature_categories.data.repository.FakeCategoryRepository
import cz.vratislavjindra.alzacasestudy.feature_categories.domain.model.Category
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class GetAllCategoriesTest {

    private lateinit var getAllCategories: GetAllCategories
    private lateinit var fakeCategoryRepository: FakeCategoryRepository

    @Before
    fun setUp() {
        fakeCategoryRepository = FakeCategoryRepository()
        getAllCategories = GetAllCategories(categoryRepository = fakeCategoryRepository)
        val categoriesToInsert = mutableListOf<Category>()
        ('a'..'z').forEachIndexed { index, c ->
            categoriesToInsert.add(
                element = Category(
                    id = index,
                    name = c.toString(),
                    imageUrl = null,
                    order = index
                )
            )
        }
        categoriesToInsert.shuffle()
        runBlocking {
            categoriesToInsert.forEach { fakeCategoryRepository.insertCategory(category = it) }
        }
    }

    @Test
    fun `Get all categories without error`() = runBlocking {
        val getAllCategoriesResult = getAllCategories()
        val resultList = getAllCategoriesResult.toList()
        assertNotNull(resultList)
        assertTrue(resultList.size == 3)
        assertTrue(resultList[0] is Resource.Loading)
        assertNull((resultList[0] as Resource.Loading).data)
        assertTrue(resultList[1] is Resource.Loading)
        assertNotNull((resultList[1] as Resource.Loading).data)
        assertEquals(0, (resultList[1] as Resource.Loading).data?.size)
        assertTrue(resultList[2] is Resource.Success)
        assertNotNull((resultList[2] as Resource.Success).data)
        assertTrue((resultList[2] as Resource.Success).data.isNotEmpty())
        assertEquals(
            26,
            (resultList[2] as Resource.Success).data.size
        )
    }

    @Test
    fun `Get all categories with forced error`() = runBlocking {
        fakeCategoryRepository.returnError = true
        val getAllCategoriesResult = getAllCategories()
        val resultList = getAllCategoriesResult.toList()
        assertNotNull(resultList)
        assertTrue(resultList.size == 3)
        assertTrue(resultList[0] is Resource.Loading)
        assertNull((resultList[0] as Resource.Loading).data)
        assertTrue(resultList[1] is Resource.Loading)
        assertNotNull((resultList[1] as Resource.Loading).data)
        assertEquals(0, (resultList[1] as Resource.Loading).data?.size)
        assertTrue(resultList[2] is Resource.Error)
        assertNotNull((resultList[2] as Resource.Error).errorMessage)
        assertEquals(
            R.string.error_load_categories,
            (resultList[2] as Resource.Error).errorMessage.messageId
        )
    }
}