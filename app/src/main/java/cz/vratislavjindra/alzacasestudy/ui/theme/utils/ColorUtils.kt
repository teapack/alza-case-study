package cz.vratislavjindra.alzacasestudy.ui.theme.utils

import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.ln

/**
 * Returns the new background [Color] to use, representing the original background [color] with an
 * overlay corresponding to [elevation] applied. The overlay will only be applied to
 * [ColorScheme.surface].
 */
internal fun ColorScheme.applyTonalElevation(backgroundColor: Color, elevation: Dp): Color {
    return if (backgroundColor == surface) {
        surfaceColorAtElevation(elevation = elevation)
    } else {
        backgroundColor
    }
}

/**
 * Returns the [ColorScheme.surface] color with an alpha of the [ColorScheme.surfaceTint] color
 * overlaid on top of it. Computes the surface tonal color at different elevation levels.
 *
 * @param elevation Elevation value used to compute alpha of the color overlay layer.
 */
internal fun ColorScheme.surfaceColorAtElevation(elevation: Dp): Color {
    if (elevation == 0.dp) return surface
    val alpha = ((4.5f * ln(x = elevation.value + 1)) + 2f) / 100f
    return surfaceTint.copy(alpha = alpha).compositeOver(background = surface)
}
