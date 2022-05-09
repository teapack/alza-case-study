package cz.vratislavjindra.alzacasestudy.ui.components.snackbar

data class SnackbarAction(
    val actionLabel: String,
    val action: () -> Unit
)