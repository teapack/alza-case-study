package cz.vratislavjindra.alzacasestudy.feature_categories.presentation.categories_list

import cz.vratislavjindra.alzacasestudy.core.coroutine.MainDispatcherRule
import cz.vratislavjindra.alzacasestudy.feature_categories.data.repository.FakeCategoryRepository
import cz.vratislavjindra.alzacasestudy.feature_categories.domain.model.Category
import cz.vratislavjindra.alzacasestudy.feature_categories.domain.use_case.GetAllCategories
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CategoriesViewModelTest {

    private lateinit var categoriesViewModel: CategoriesListViewModel
    private lateinit var getAllCategories: GetAllCategories
    private lateinit var fakeCategoryRepository: FakeCategoryRepository

    @ExperimentalCoroutinesApi
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        fakeCategoryRepository = FakeCategoryRepository()
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
        getAllCategories = GetAllCategories(categoryRepository = fakeCategoryRepository)
        categoriesViewModel = CategoriesListViewModel(getAllCategoriesUseCase = getAllCategories)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `UI state has empty categories, is loading and has no error message when ViewModel is initiated`() = runTest {
        assertEquals(0, categoriesViewModel.uiState.value.categories.size)
        assertTrue(categoriesViewModel.uiState.value.loading)
        assertNull(categoriesViewModel.uiState.value.errorMessage)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `UI state has categories, is not loading and has no error message when ViewModel loads categories after a while`() = runTest {
        `UI state has empty categories, is loading and has no error message when ViewModel is initiated`()
        delay(timeMillis = 1_000)
        assertEquals(26, categoriesViewModel.uiState.value.categories.size)
        assertFalse(categoriesViewModel.uiState.value.loading)
        assertNull(categoriesViewModel.uiState.value.errorMessage)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `UI state has cached categories, is loading and has no error message when categories are refreshed manually`() = runTest {
        `UI state has categories, is not loading and has no error message when ViewModel loads categories after a while`()
        categoriesViewModel.refreshCategories()
        assertEquals(26, categoriesViewModel.uiState.value.categories.size)
        assertTrue(categoriesViewModel.uiState.value.loading)
        assertNull(categoriesViewModel.uiState.value.errorMessage)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `UI state stops loading after refreshing categories manually after a while`() = runTest {
        `UI state has cached categories, is loading and has no error message when categories are refreshed manually`()
        delay(timeMillis = 1_000)
        assertEquals(26, categoriesViewModel.uiState.value.categories.size)
        assertFalse(categoriesViewModel.uiState.value.loading)
        assertNull(categoriesViewModel.uiState.value.errorMessage)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `UI state correctly stores error, isn't loading and does not have any categories when use case returns error`() = runTest {
        fakeCategoryRepository.returnError = true
        categoriesViewModel.refreshCategories()
        delay(timeMillis = 1_000)
        assertEquals(0, categoriesViewModel.uiState.value.categories.size)
        assertFalse(categoriesViewModel.uiState.value.loading)
        assertNotNull(categoriesViewModel.uiState.value.errorMessage)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `UI state correctly restores after refreshing categories after error`() = runTest {
        `UI state correctly stores error, isn't loading and does not have any categories when use case returns error`()
        fakeCategoryRepository.returnError = false
        categoriesViewModel.refreshCategories()
        assertEquals(0, categoriesViewModel.uiState.value.categories.size)
        assertTrue(categoriesViewModel.uiState.value.loading)
        assertNull(categoriesViewModel.uiState.value.errorMessage)
        delay(timeMillis = 1_000)
        assertEquals(26, categoriesViewModel.uiState.value.categories.size)
        assertFalse(categoriesViewModel.uiState.value.loading)
        assertNull(categoriesViewModel.uiState.value.errorMessage)
    }
}