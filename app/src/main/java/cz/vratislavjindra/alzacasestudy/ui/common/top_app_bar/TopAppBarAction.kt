package cz.vratislavjindra.alzacasestudy.ui.common.top_app_bar

import androidx.compose.ui.graphics.vector.ImageVector

data class TopAppBarAction(
    val icon: ImageVector,
    val contentDescription: String,
    val onClick: () -> Unit
)