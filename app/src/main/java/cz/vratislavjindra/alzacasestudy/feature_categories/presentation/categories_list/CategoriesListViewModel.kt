package cz.vratislavjindra.alzacasestudy.feature_categories.presentation.categories_list

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.vratislavjindra.alzacasestudy.core.util.AlzaError
import cz.vratislavjindra.alzacasestudy.core.util.Resource
import cz.vratislavjindra.alzacasestudy.feature_categories.domain.use_case.GetAllCategories
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriesListViewModel @Inject constructor(
    private val getAllCategoriesUseCase: GetAllCategories
): ViewModel() {

    private val _state = mutableStateOf(value = CategoriesListScreenState())
    val state: State<CategoriesListScreenState> = _state

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    sealed class UiEvent {

        data class ShowSnackbar(val error: AlzaError, val message: String): UiEvent()
    }

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
                        is Resource.Success -> {
                            _state.value = state.value.copy(
                                loading = false,
                                categories = result.data ?: emptyList()
                            )
                        }
                        is Resource.Loading -> {
                            // TODO I could also show these categories: state.value.categories
                            _state.value = state.value.copy(
                                loading = true,
                                categories = result.data ?: emptyList()
                            )
                        }
                        is Resource.Error -> {
                            // TODO I could also show these categories: state.value.categories
                            _state.value = state.value.copy(
                                loading = false,
                                categories = result.data ?: emptyList()
                            )
                            _eventFlow.emit(
                                value = UiEvent.ShowSnackbar(
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