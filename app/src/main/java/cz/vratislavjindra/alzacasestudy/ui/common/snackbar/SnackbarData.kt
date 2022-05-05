package cz.vratislavjindra.alzacasestudy.ui.common.snackbar

data class SnackbarData(
    val message: String,
    val snackbarAction: SnackbarAction? = null,
    val dismissAction: (() -> Unit)? = null
)