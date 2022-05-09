package cz.vratislavjindra.alzacasestudy.feature_products.presentation.product_detail

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.vratislavjindra.alzacasestudy.core.util.AlzaError
import cz.vratislavjindra.alzacasestudy.core.util.Resource
import cz.vratislavjindra.alzacasestudy.feature_products.domain.use_case.GetProductDetail
import cz.vratislavjindra.alzacasestudy.ui.Screen
import cz.vratislavjindra.alzacasestudy.ui.components.snackbar.SnackbarData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val getProductDetailUseCase: GetProductDetail,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = mutableStateOf(value = ProductDetailScreenState())
    val state: State<ProductDetailScreenState> = _state

    private val snackbarChannel = Channel<SnackbarData>(capacity = Channel.CONFLATED)
    val snackbarFlow = snackbarChannel.receiveAsFlow()

    private var getProductDetailJob: Job? = null

    init {
        getProductDetail(
            productId = savedStateHandle.get<Int>(Screen.ProductDetailScreen.navArgument!!)
        )
    }

    fun getProductDetail(productId: Int?) {
        if (productId == null) {
            _state.value = state.value.copy(loading = false)
            viewModelScope.launch {
                snackbarChannel.send(
                    element = SnackbarData.ErrorSnackbarData(error = AlzaError.INVALID_PRODUCT)
                )
            }
        } else {
            _state.value = state.value.copy(loading = true)
            getProductDetailJob?.cancel()
            getProductDetailJob = viewModelScope.launch {
                getProductDetailUseCase(productId = productId)
                    .onEach { result ->
                        when (result) {
                            is Resource.Success -> _state.value = state.value.copy(
                                loading = false,
                                product = result.data
                            )
                            // TODO I could also show this product: state.value.product
                            is Resource.Loading -> _state.value = state.value.copy(
                                loading = true,
                                product = result.data
                            )
                            // TODO I could also show this product: state.value.product
                            is Resource.Error -> {
                                _state.value = state.value.copy(
                                    loading = false,
                                    product = result.data
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