package cz.vratislavjindra.alzacasestudy.ui.components.loading

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.unit.dp
import com.google.accompanist.placeholder.PlaceholderDefaults
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.placeholder
import com.google.accompanist.placeholder.shimmer

fun Modifier.shimmerPlaceholder(
    visible: Boolean,
    color: Color = Color.Unspecified,
    shape: Shape? = null
): Modifier = composed {
    this.placeholder(
        visible = visible,
        color = if (color.isSpecified) color else {
            if (isSystemInDarkTheme()) {
                MaterialTheme.colorScheme.surface
            } else {
                MaterialTheme.colorScheme.surface
            }
        },
        shape = shape ?: RoundedCornerShape(size = 16.dp),
        highlight = PlaceholderHighlight.shimmer(
            highlightColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.75f),
            animationSpec = PlaceholderDefaults.shimmerAnimationSpec,
            progressForMaxAlpha = 0.6f,
        )
    )
}