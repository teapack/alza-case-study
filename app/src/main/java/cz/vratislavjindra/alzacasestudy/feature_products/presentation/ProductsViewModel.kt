package cz.vratislavjindra.alzacasestudy.feature_products.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.vratislavjindra.alzacasestudy.R
import cz.vratislavjindra.alzacasestudy.core.data.Resource
import cz.vratislavjindra.alzacasestudy.core.utils.ErrorMessage
import cz.vratislavjindra.alzacasestudy.feature_products.domain.model.Product
import cz.vratislavjindra.alzacasestudy.feature_products.domain.use_case.GetProductDetail
import cz.vratislavjindra.alzacasestudy.feature_products.domain.use_case.GetProducts
import cz.vratislavjindra.alzacasestudy.ui.components.snackbar.SnackbarData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * UI state for the Products route.
 *
 * This is derived from [ProductsViewModelState], but split into two possible subclasses to more
 * precisely represent the state available to render the UI.
 */
sealed interface ProductsUiState {

    val productsLoading: Boolean
    val productDetailErrorMessage: ErrorMessage?
    val productsListErrorMessage: ErrorMessage?

    /**
     * There are no products to render.
     *
     * This could either be because they are still loading or they failed to load, and we are
     * waiting to reload them.
     */
    data class NoProducts(
        override val productsLoading: Boolean,
        override val productDetailErrorMessage: ErrorMessage?,
        override val productsListErrorMessage: ErrorMessage?
    ) : ProductsUiState

    /**
     * There are products to render, as contained in [products].
     *
     * There is not guaranteed to be a [selectedProduct].
     */
    data class HasProducts(
        val products: List<Product>,
        val selectedProduct: Product?,
        val productDetailLoading: Boolean,
        override val productsLoading: Boolean,
        override val productDetailErrorMessage: ErrorMessage?,
        override val productsListErrorMessage: ErrorMessage?
    ) : ProductsUiState
}

/**
 * An internal representation of the Products route state, in a raw form.
 */
private data class ProductsViewModelState(
    val categoryId: Int? = null,
    val products: List<Product>? = null,
    val productsLoading: Boolean = false,
    val productsListErrorMessage: ErrorMessage? = null,
    val selectedProductId: Int? = null,
    val productDetailLoading: Boolean = false,
    val productDetailErrorMessage: ErrorMessage? = null
) {

    /**
     * Converts this [ProductsViewModelState] into a more strongly typed [ProductsUiState] for
     * driving the UI.
     */
    fun toUiState(): ProductsUiState =
        if (products == null) {
            ProductsUiState.NoProducts(
                productsLoading = productsLoading,
                productDetailErrorMessage = productDetailErrorMessage,
                productsListErrorMessage = productsListErrorMessage
            )
        } else {
            ProductsUiState.HasProducts(
                products = products,
                // Determine the selected product. If there is none (or that product isn't in the
                // current products list), default to null.
                selectedProduct = products.find { it.id == selectedProductId },
                productDetailLoading = productDetailLoading,
                productsLoading = productsLoading,
                productDetailErrorMessage = productDetailErrorMessage,
                productsListErrorMessage = productsListErrorMessage
            )
        }
}

@HiltViewModel
class ProductsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getProductsUseCase: GetProducts,
    private val getProductDetailUseCase: GetProductDetail
) : ViewModel() {

    private val viewModelState = MutableStateFlow(
        value = ProductsViewModelState(
            productsLoading = true,
            productDetailLoading = false
        )
    )

    // UI state exposed to the UI.
    val uiState = viewModelState
        .map { it.toUiState() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = viewModelState.value.toUiState()
        )

    private val snackbarChannel = Channel<SnackbarData>(capacity = Channel.CONFLATED)
    val snackbarFlow = snackbarChannel.receiveAsFlow()

    private var getProductsJob: Job? = null
    private var getProductDetailJob: Job? = null

    init {
        viewModelState.update { it.copy(categoryId = savedStateHandle.get<Int>("category_id")) }
        refreshProducts()
    }

    fun refreshProducts() {
        viewModelState.value.categoryId.let { categoryId ->
            if (categoryId == null) {
                viewModelState.update {
                    it.copy(
                        productsLoading = false,
                        productsListErrorMessage = ErrorMessage(
                            messageId = R.string.error_invalid_category
                        )
                    )
                }
            } else {
                viewModelState.update {
                    it.copy(
                        productsLoading = true,
                        productsListErrorMessage = null
                    )
                }
                getProductsJob?.cancel()
                getProductsJob = viewModelScope.launch {
                    getProductsUseCase(categoryId = categoryId)
                        .onEach { result ->
                            viewModelState.update {
                                when (result) {
                                    is Resource.Success -> it.copy(
                                        products = result.data,
                                        productsLoading = false
                                    )
                                    is Resource.Loading -> it.copy(
                                        products = result.data ?: emptyList(),
                                        productsLoading = true
                                    )
                                    is Resource.Error ->
                                        it.copy(
                                            productsLoading = false,
                                            productsListErrorMessage = result.errorMessage
                                        )
                                }
                            }
                        }
                        .launchIn(scope = this)
                }
            }
        }
    }

    fun refreshProductDetail() {
        viewModelState.value.selectedProductId.let { selectedProductId ->
            if (selectedProductId == null) {
                viewModelState.update {
                    it.copy(
                        productDetailLoading = false,
                        productDetailErrorMessage = ErrorMessage(
                            messageId = R.string.error_invalid_product
                        )
                    )
                }
            } else {
                viewModelState.update {
                    it.copy(
                        productDetailLoading = true,
                        productDetailErrorMessage = null
                    )
                }
                getProductDetailJob?.cancel()
                getProductDetailJob = viewModelScope.launch {
                    getProductDetailUseCase(productId = selectedProductId)
                        .onEach { result ->
                            viewModelState.update {
                                when (result) {
                                    is Resource.Success -> {
                                        // Refresh the product detail in the products list as well.
                                        val productIndex = it.products?.indexOfFirst { product ->
                                            product.id == selectedProductId
                                        }
                                        val products = it.products?.filterNot { product ->
                                            product.id == selectedProductId
                                        }?.toMutableList()
                                        productIndex.let { index ->
                                            if (index != null) {
                                                products?.add(
                                                    index = index,
                                                    element = result.data
                                                )
                                                it.copy(
                                                    productDetailLoading = false,
                                                    products = products
                                                )
                                            } else {
                                                it.copy(
                                                    productDetailLoading = false,
                                                    productDetailErrorMessage = ErrorMessage(
                                                        messageId = R.string
                                                            .error_load_product_detail
                                                    )
                                                )
                                            }
                                        }
                                    }
                                    is Resource.Loading -> it.copy(
                                        productDetailLoading = true
                                    )
                                    is Resource.Error -> it.copy(
                                        productDetailLoading = false,
                                        productDetailErrorMessage = result.errorMessage
                                    )
                                }
                            }
                        }
                        .launchIn(scope = this)
                }
            }
        }
    }

    /**
     * Selects the given product to view more information about it.
     */
    fun selectProduct(productId: Int) {
        viewModelState.update { it.copy(selectedProductId = productId) }
    }

    /**
     * Notify that the user interacted with the product list.
     */
    fun interactedWithProductList() {
        viewModelState.update { it.copy(selectedProductId = null) }
    }

    /**
     * Notify that the user interacted with the product list.
     */
    fun interactedWithProductAction() {
        viewModelScope.launch {
            snackbarChannel.send(
                element = SnackbarData(
                    messageResId = R.string.error_feature_not_implemented_yet
                )
            )
        }
    }
}