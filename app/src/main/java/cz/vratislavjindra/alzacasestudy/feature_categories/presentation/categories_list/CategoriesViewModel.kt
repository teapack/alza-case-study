package cz.vratislavjindra.alzacasestudy.feature_categories.presentation.categories_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.vratislavjindra.alzacasestudy.core.data.Resource
import cz.vratislavjindra.alzacasestudy.core.utils.ErrorMessage
import cz.vratislavjindra.alzacasestudy.feature_categories.domain.model.Category
import cz.vratislavjindra.alzacasestudy.feature_categories.domain.use_case.GetAllCategories
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * UI state for the Categories screen.
 */
data class CategoriesUiState(
    val categories: List<Category> = emptyList(),
    val loading: Boolean = false,
    val errorMessage: ErrorMessage? = null
)

@HiltViewModel
class CategoriesListViewModel @Inject constructor(
    private val getAllCategoriesUseCase: GetAllCategories
) : ViewModel() {

    // UI state exposed to the UI.
    private val _uiState = MutableStateFlow(value = CategoriesUiState())
    val uiState: StateFlow<CategoriesUiState> = _uiState.asStateFlow()

    private var getAllCategoriesJob: Job? = null

    init {
        refreshCategories()
    }

    /**
     * Refresh categories and update the UI state accordingly.
     */
    fun refreshCategories() {
        _uiState.update {
            it.copy(
                loading = true,
                errorMessage = null
            )
        }
        getAllCategoriesJob?.cancel()
        getAllCategoriesJob = viewModelScope.launch {
            getAllCategoriesUseCase()
                .onEach { result ->
                    when (result) {
                        is Resource.Success -> _uiState.update {
                            it.copy(
                                loading = false,
                                categories = result.data
                            )
                        }
                        is Resource.Loading -> _uiState.update {
                            it.copy(
                                loading = true,
                                categories = result.data ?: uiState.value.categories
                            )
                        }
                        is Resource.Error -> _uiState.update {
                            it.copy(
                                loading = false,
                                categories = result.data ?: uiState.value.categories,
                                errorMessage = result.errorMessage
                            )
                        }
                    }
                }
                .launchIn(scope = this)
        }
    }
}