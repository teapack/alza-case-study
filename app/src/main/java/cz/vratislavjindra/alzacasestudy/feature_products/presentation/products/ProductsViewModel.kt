package cz.vratislavjindra.alzacasestudy.feature_products.presentation.products

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.vratislavjindra.alzacasestudy.Screen
import cz.vratislavjindra.alzacasestudy.core.util.AlzaError
import cz.vratislavjindra.alzacasestudy.core.util.Resource
import cz.vratislavjindra.alzacasestudy.core.util.UiEvent
import cz.vratislavjindra.alzacasestudy.feature_products.domain.model.ProductOverview
import cz.vratislavjindra.alzacasestudy.feature_products.domain.use_case.GetProducts
import cz.vratislavjindra.alzacasestudy.ui.common.snackbar.SnackbarData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val getProductsUseCase: GetProducts,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = mutableStateOf(value = ProductsScreenState())
    val state: State<ProductsScreenState> = _state

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val productClickedChannel = Channel<SnackbarData>(capacity = Channel.CONFLATED)
    val productClickedFlow = productClickedChannel.receiveAsFlow()

    private var getProductsJob: Job? = null

    init {
        getProducts(categoryId = savedStateHandle.get<Int>(Screen.ProductsScreen.argument1!!))
    }

    fun getProducts(categoryId: Int?) {
        if (categoryId == null) {
            _state.value = state.value.copy(
                loading = false,
                products = emptyList()
            )
            viewModelScope.launch {
                _eventFlow.emit(
                    value = UiEvent.ShowErrorSnackbar(error = AlzaError.INVALID_CATEGORY)
                )
            }
        } else {
            _state.value = state.value.copy(loading = true)
            getProductsJob?.cancel()
            getProductsJob = viewModelScope.launch {
                getProductsUseCase(categoryId = categoryId)
                    .onEach { result ->
                        when (result) {
                            is Resource.Success -> {
                                _state.value = state.value.copy(
                                    loading = false,
                                    products = result.data ?: emptyList()
                                )
                            }
                            is Resource.Loading -> {
                                // TODO I could also show these products: state.value.products
                                _state.value = state.value.copy(
                                    loading = true,
                                    products = result.data ?: emptyList()
                                )
                            }
                            is Resource.Error -> {
                                // TODO I could also show these products: state.value.products
                                _state.value = state.value.copy(
                                    loading = false,
                                    products = result.data ?: emptyList()
                                )
                                _eventFlow.emit(
                                    value = UiEvent.ShowErrorSnackbarWithCustomMessage(
                                        error = result.error,
                                        message = result.errorMessage
                                    )
                                )
                                productClickedChannel.send(
                                    element = SnackbarData(message = result.errorMessage)
                                )
                            }
                        }
                    }
                    .launchIn(scope = this)
            }
        }
    }

    fun onProductClick(product: ProductOverview) {
        viewModelScope.launch {
            productClickedChannel.send(element = SnackbarData(message = product.name))
        }
    }
}