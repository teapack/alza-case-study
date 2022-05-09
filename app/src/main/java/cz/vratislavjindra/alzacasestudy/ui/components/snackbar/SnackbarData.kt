package cz.vratislavjindra.alzacasestudy.ui.components.snackbar

import cz.vratislavjindra.alzacasestudy.core.util.AlzaError

sealed class SnackbarData(
    val snackbarAction: SnackbarAction?,
    val dismissAction: (() -> Unit)?
) {

    data class MessageSnackbarData(
        val message: String,
        val onAction: SnackbarAction? = null,
        val onDismiss: (() -> Unit)? = null
    ): SnackbarData(
        snackbarAction = onAction,
        dismissAction = onDismiss
    )

    data class ErrorSnackbarData(
        val error: AlzaError,
        val message: String? = null,
        val onAction: SnackbarAction? = null,
        val onDismiss: (() -> Unit)? = null
    ): SnackbarData(
        snackbarAction = onAction,
        dismissAction = onDismiss
    )
}