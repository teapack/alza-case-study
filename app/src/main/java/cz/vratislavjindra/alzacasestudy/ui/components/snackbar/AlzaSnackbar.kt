package cz.vratislavjindra.alzacasestudy.ui.components.snackbar

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Snackbar
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

@Composable
fun AlzaSnackbar(snackbarData: androidx.compose.material3.SnackbarData) {
    Snackbar(
        snackbarData = snackbarData,
        shape = RoundedCornerShape(size = 32.dp)
    )
}