package cz.vratislavjindra.alzacasestudy.ui.common.top_app_bar

import androidx.compose.ui.graphics.vector.ImageVector

data class TopAppBarAction(
    val icon: ImageVector,
    val rotateIcon: Boolean = false,
    val contentDescription: String,
    val onClick: () -> Unit
)