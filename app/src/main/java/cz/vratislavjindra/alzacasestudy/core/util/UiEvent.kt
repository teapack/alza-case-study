package cz.vratislavjindra.alzacasestudy.core.util

sealed class UiEvent {

    data class ShowErrorSnackbar(val error: AlzaError) : UiEvent()

    data class ShowErrorSnackbarWithCustomMessage(val error: AlzaError, val message: String) :
        UiEvent()
}