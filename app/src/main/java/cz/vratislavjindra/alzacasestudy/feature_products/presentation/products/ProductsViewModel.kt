package cz.vratislavjindra.alzacasestudy.feature_products.presentation.products

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.vratislavjindra.alzacasestudy.Screen
import cz.vratislavjindra.alzacasestudy.core.util.AlzaError
import cz.vratislavjindra.alzacasestudy.core.util.Resource
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

    private val snackbarChannel = Channel<SnackbarData>(capacity = Channel.CONFLATED)
    val snackbarFlow = snackbarChannel.receiveAsFlow()

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
                snackbarChannel.send(
                    element = SnackbarData.ErrorSnackbarData(error = AlzaError.INVALID_CATEGORY)
                )
            }
        } else {
            _state.value = state.value.copy(loading = true)
            getProductsJob?.cancel()
            getProductsJob = viewModelScope.launch {
                getProductsUseCase(categoryId = categoryId)
                    .onEach { result ->
                        when (result) {
                            is Resource.Success -> _state.value = state.value.copy(
                                loading = false,
                                products = result.data ?: emptyList()
                            )
                            // TODO I could also show these products: state.value.products
                            is Resource.Loading -> _state.value = state.value.copy(
                                loading = true,
                                products = result.data ?: emptyList()
                            )
                            // TODO I could also show these products: state.value.products
                            is Resource.Error -> {
                                _state.value = state.value.copy(
                                    loading = false,
                                    products = result.data ?: emptyList()
                                )
                                snackbarChannel.send(
                                    element = SnackbarData.ErrorSnackbarData(
                                        error = result.error,
                                        message = result.errorMessage
                                    )
                                )
                            }
                        }
                    }
                    .launchIn(scope = this)
            }
        }
    }

    fun onProductActionClick() {
        viewModelScope.launch {
            snackbarChannel.send(
                element = SnackbarData.ErrorSnackbarData(
                    error = AlzaError.FEATURE_NOT_IMPLEMENTED_YET
                )
            )
        }
    }
}