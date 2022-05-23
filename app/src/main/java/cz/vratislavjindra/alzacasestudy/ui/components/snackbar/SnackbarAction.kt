package cz.vratislavjindra.alzacasestudy.ui.components.snackbar

import androidx.annotation.StringRes

data class SnackbarAction(
    @StringRes val actionLabelResId: Int,
    val action: () -> Unit
)