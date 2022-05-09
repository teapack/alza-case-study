package cz.vratislavjindra.alzacasestudy.ui.components.top_app_bar

import androidx.compose.ui.graphics.vector.ImageVector

data class TopAppBarAction(
    val icon: ImageVector,
    val rotateIcon: Boolean = false,
    val contentDescription: String,
    val onClick: () -> Unit
)