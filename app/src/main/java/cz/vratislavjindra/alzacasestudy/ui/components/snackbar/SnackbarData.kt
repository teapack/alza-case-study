package cz.vratislavjindra.alzacasestudy.ui.components.snackbar

import androidx.annotation.StringRes

data class SnackbarData(
    @StringRes val messageResId: Int,
    val snackbarAction: SnackbarAction? = null,
    val onDismiss: (() -> Unit)? = null
)