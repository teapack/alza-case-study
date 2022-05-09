package cz.vratislavjindra.alzacasestudy.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.vratislavjindra.alzacasestudy.core.util.AlzaError
import cz.vratislavjindra.alzacasestudy.core.util.Resource
import cz.vratislavjindra.alzacasestudy.feature_products.domain.model.Category
import cz.vratislavjindra.alzacasestudy.feature_products.domain.model.Product
import cz.vratislavjindra.alzacasestudy.feature_products.domain.model.ProductListItem
import cz.vratislavjindra.alzacasestudy.feature_products.domain.use_case.GetAllCategories
import cz.vratislavjindra.alzacasestudy.feature_products.domain.use_case.GetProductDetail
import cz.vratislavjindra.alzacasestudy.feature_products.domain.use_case.GetProducts
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * UI state for the Home route.
 *
 * This is derived from [HomeViewModelState], but split into two possible subclasses to more
 * precisely represent the state available to render the UI.
 */
sealed interface HomeUiState {

    val categoriesLoading: Boolean
    val productsLoading: Boolean
    val productDetailLoading: Boolean
    val errors: List<AlzaError>

    /**
     * There are no categories to render.
     *
     * This could either be because they are still loading or they failed to load, and we are
     * waiting to reload them.
     */
    data class NoCategories(
        override val categoriesLoading: Boolean,
        override val productsLoading: Boolean,
        override val productDetailLoading: Boolean,
        override val errors: List<AlzaError>
    ) : HomeUiState

    /**
     * There are categories to render, as contained in [categories].
     *
     * This could either be because they are still loading or they failed to load, and we are
     * waiting to reload them.
     */
    data class HasCategories(
        val categories: List<Category>,
        override val categoriesLoading: Boolean,
        override val productsLoading: Boolean,
        override val productDetailLoading: Boolean,
        override val errors: List<AlzaError>
    ) : HomeUiState

    /**
     * There are categories to render, as contained in [categories], and no products to render.
     */
    data class HasCategoriesNoProducts(
        val categories: List<Category>,
        val selectedCategoryId: Int,
        override val categoriesLoading: Boolean,
        override val productsLoading: Boolean,
        override val productDetailLoading: Boolean,
        override val errors: List<AlzaError>
    ) : HomeUiState

    /**
     * There are categories to render, as contained in [categories], and products to render, as
     * contained in [products].
     */
    data class HasCategoriesHasProducts(
        val categories: List<Category>,
        val selectedCategoryId: Int,
        val products: List<ProductListItem>,
        override val categoriesLoading: Boolean,
        override val productsLoading: Boolean,
        override val productDetailLoading: Boolean,
        override val errors: List<AlzaError>
    ) : HomeUiState

    /**
     * There are categories to render, as contained in [categories], and products to render, as
     * contained in [products].
     */
    data class HasCategoriesHasProductsNoProductDetail(
        val categories: List<Category>,
        val selectedCategoryId: Int,
        val products: List<ProductListItem>,
        val selectedProductId: Int,
        override val categoriesLoading: Boolean,
        override val productsLoading: Boolean,
        override val productDetailLoading: Boolean,
        override val errors: List<AlzaError>
    ) : HomeUiState

    /**
     * There are categories to render, as contained in [categories], and products to render, as
     * contained in [products], and a product detail to render.
     */
    data class HasCategoriesHasProductsHasProductDetail(
        val categories: List<Category>,
        val selectedCategoryId: Int,
        val products: List<ProductListItem>,
        val selectedProductId: Int,
        val selectedProduct: Product,
        override val categoriesLoading: Boolean,
        override val productsLoading: Boolean,
        override val productDetailLoading: Boolean,
        override val errors: List<AlzaError>
    ) : HomeUiState
}

/**
 * An internal representation of the Home route state, in a raw form.
 */
private data class HomeViewModelState(
    val categories: List<Category>? = null,
    val selectedCategoryId: Int? = null, // TODO back selectedCategoryId in a SavedStateHandle
    val products: List<ProductListItem>? = null,
    val selectedProductId: Int? = null, // TODO back selectedProductId in a SavedStateHandle
    val selectedProduct: Product? = null,
    val categoriesLoading: Boolean = false,
    val productsLoading: Boolean = false,
    val productDetailLoading: Boolean = false,
    val errors: List<AlzaError> = emptyList()
) {

    /**
     * Converts this [HomeViewModelState] into a more strongly typed [HomeUiState] for driving the
     * UI.
     */
    fun toUiState(): HomeUiState =
        if (categories == null) {
            HomeUiState.NoCategories(
                categoriesLoading = categoriesLoading,
                productsLoading = productsLoading,
                productDetailLoading = productDetailLoading,
                errors = errors
            )
        } else if (selectedCategoryId == null) {
            HomeUiState.HasCategories(
                categories = categories,
                categoriesLoading = categoriesLoading,
                productsLoading = productsLoading,
                productDetailLoading = productDetailLoading,
                errors = errors
            )
        } else if (products == null) {
            HomeUiState.HasCategoriesNoProducts(
                categories = categories,
                selectedCategoryId = selectedCategoryId,
                categoriesLoading = categoriesLoading,
                productsLoading = productsLoading,
                productDetailLoading = productDetailLoading,
                errors = errors
            )
        } else if (selectedProductId == null) {
            HomeUiState.HasCategoriesHasProducts(
                categories = categories,
                selectedCategoryId = selectedCategoryId,
                products = products,
                categoriesLoading = categoriesLoading,
                productsLoading = productsLoading,
                productDetailLoading = productDetailLoading,
                errors = errors
            )
        } else if (selectedProduct == null) {
            HomeUiState.HasCategoriesHasProductsNoProductDetail(
                categories = categories,
                selectedCategoryId = selectedCategoryId,
                products = products,
                selectedProductId = selectedProductId,
                categoriesLoading = categoriesLoading,
                productsLoading = productsLoading,
                productDetailLoading = productDetailLoading,
                errors = errors
            )
        } else {
            HomeUiState.HasCategoriesHasProductsHasProductDetail(
                categories = categories,
                selectedCategoryId = selectedCategoryId,
                products = products,
                selectedProductId = selectedProductId,
                selectedProduct = selectedProduct,
                categoriesLoading = categoriesLoading,
                productsLoading = productsLoading,
                productDetailLoading = productDetailLoading,
                errors = errors
            )
        }
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCategoriesUseCase: GetAllCategories,
    private val getProductsUseCase: GetProducts,
    private val getProductDetailUseCase: GetProductDetail
) : ViewModel() {

    private val viewModelState = MutableStateFlow(
        value = HomeViewModelState(
            categoriesLoading = true,
            productsLoading = false,
            productDetailLoading = false
        )
    )

    // UI state exposed to the UI.
    val uiState = viewModelState
        .map { it.toUiState() }
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            viewModelState.value.toUiState()
        )

    private var getCategoriesJob: Job? = null
    private var getProductsJob: Job? = null
    private var getProductDetailJob: Job? = null

    init {
        refreshCategories()
//        viewModelScope.launch {
//            postsRepository.observeFavorites().collect { favorites ->
//                viewModelState.update { it.copy(favorites = favorites) }
//            }
//        }
    }

    fun refreshCategories() {
        viewModelState.update { it.copy(categoriesLoading = true) }
        getCategoriesJob?.cancel()
        getCategoriesJob = viewModelScope.launch {
            getCategoriesUseCase()
                .onEach { result ->
                    viewModelState.update {
                        when (result) {
                            is Resource.Success -> it.copy(
                                categories = result.data,
                                categoriesLoading = false
                            )
                            is Resource.Loading -> it.copy(
                                categories = result.data,
                                categoriesLoading = true
                            )
                            is Resource.Error -> {
                                val errors = it.errors + result.error
                                it.copy(
                                    errors = errors,
                                    categoriesLoading = false
                                )
                            }
                        }
                    }
                }
                .launchIn(scope = this)
        }
    }

    fun loadProducts() {
        viewModelState.update { it.copy(productsLoading = true) }
        getProductsJob?.cancel()
        getProductsJob = viewModelScope.launch {
            getProductsUseCase(categoryId = viewModelState.value.selectedCategoryId)
                .onEach { result ->
                    viewModelState.update {
                        when (result) {
                            is Resource.Success -> it.copy(
                                products = result.data,
                                productsLoading = false
                            )
                            is Resource.Loading -> it.copy(
                                products = result.data,
                                productsLoading = true
                            )
                            is Resource.Error -> {
                                val errors = it.errors + result.error
                                it.copy(
                                    errors = errors,
                                    productsLoading = false
                                )
                            }
                        }
                    }
                }
                .launchIn(scope = this)
        }
    }

    fun loadProductDetail() {
        viewModelState.update { it.copy(productDetailLoading = true) }
        getProductDetailJob?.cancel()
        getProductDetailJob = viewModelScope.launch {
            getProductDetailUseCase(productId = viewModelState.value.selectedProductId)
                .onEach { result ->
                    viewModelState.update {
                        when (result) {
                            is Resource.Success -> it.copy(
                                selectedProduct = result.data,
                                productDetailLoading = false
                            )
                            is Resource.Loading -> it.copy(
                                selectedProduct = result.data,
                                productDetailLoading = true
                            )
                            is Resource.Error -> {
                                val errors = it.errors + result.error
                                it.copy(
                                    errors = errors,
                                    productDetailLoading = false
                                )
                            }
                        }
                    }
                }
                .launchIn(scope = this)
        }
    }

    /**
     * Notify that the user selected a category.
     */
    fun selectCategory(categoryId: Int) {
        viewModelState.update {
            it.copy(
                selectedCategoryId = categoryId,
                selectedProductId = null
            )
        }
        loadProducts()
    }

    /**
     * Notify that the user selected a product.
     */
    fun selectProduct(productId: Int) {
        viewModelState.update { it.copy(selectedProductId = productId) }
        loadProductDetail()
    }

    /**
     * Notify that the user interacted with the product list.
     */
    fun interactedWithProductList() {
        viewModelState.update { it.copy(selectedCategoryId = null) }
    }

    /**
     * Notify that the user interacted with the product detail.
     */
    fun interactedWithProductDetail() {
        viewModelState.update { it.copy(selectedProductId = null) }
    }

    /**
     * Notify that the user interacted with the product action.
     */
    fun interactedWithProductAction() {
        viewModelState.update {
            val errors = it.errors + AlzaError.FEATURE_NOT_IMPLEMENTED_YET
            it.copy(errors = errors)
        }
    }
}