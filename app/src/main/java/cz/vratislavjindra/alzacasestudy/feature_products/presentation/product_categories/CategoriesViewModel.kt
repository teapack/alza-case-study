package cz.vratislavjindra.alzacasestudy.feature_products.presentation.product_categories

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.vratislavjindra.alzacasestudy.core.util.Resource
import cz.vratislavjindra.alzacasestudy.feature_products.domain.use_case.GetAllCategories
import cz.vratislavjindra.alzacasestudy.ui.common.snackbar.SnackbarData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriesListViewModel @Inject constructor(
    private val getAllCategoriesUseCase: GetAllCategories
) : ViewModel() {

    private val _state = mutableStateOf(value = CategoriesScreenState())
    val state: State<CategoriesScreenState> = _state

    private val snackbarDataChannel = Channel<SnackbarData>(capacity = Channel.CONFLATED)
    val snackbarDataFlow = snackbarDataChannel.receiveAsFlow()

    private var getAllCategoriesJob: Job? = null

    init {
        getAllCategories()
    }

    fun getAllCategories() {
        _state.value = state.value.copy(loading = true)
        getAllCategoriesJob?.cancel()
        getAllCategoriesJob = viewModelScope.launch {
            getAllCategoriesUseCase()
                .onEach { result ->
                    when (result) {
                        // TODO I could also show the previously loaded categories
                        //  (state.value.categories) instead of emptyList().
                        is Resource.Success -> _state.value = state.value.copy(
                            loading = false,
                            categories = result.data ?: emptyList()
                        )
                        is Resource.Loading -> _state.value = state.value.copy(
                            loading = true,
                            categories = result.data ?: emptyList()
                        )
                        is Resource.Error -> {
                            _state.value = state.value.copy(
                                loading = false,
                                categories = result.data ?: emptyList()
                            )
                            snackbarDataChannel.send(
                                element = SnackbarData.MessageSnackbarData(
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