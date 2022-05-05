package cz.vratislavjindra.alzacasestudy.ui.common.snackbar

data class SnackbarAction(
    val actionLabel: String,
    val action: () -> Unit
)