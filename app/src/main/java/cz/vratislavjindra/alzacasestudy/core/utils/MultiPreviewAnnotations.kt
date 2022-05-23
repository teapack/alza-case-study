package cz.vratislavjindra.alzacasestudy.core.utils

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.Preview

/**
 * Add this multi-preview annotation to a composable to render the composable in extra small and
 * extra large font size.
 */
@Preview(
    name = "small font",
    group = "font scales",
    fontScale = 0.5f,
    showBackground = true
)
@Preview(
    name = "large font",
    group = "font scales",
    fontScale = 1.5f,
    showBackground = true
)
annotation class FontScalePreviews

/**
 * Add this multi-preview annotation to a composable to render the composable on various device
 * sizes: phone, foldable, and tablet.
 */
@Preview(
    name = "phone",
    group = "devices",
    device = "spec:shape=Normal,width=360,height=640,unit=dp,dpi=480"
)
@Preview(
    name = "foldable",
    group = "devices",
    device = "spec:shape=Normal,width=673,height=841,unit=dp,dpi=480"
)
@Preview(
    name = "tablet",
    group = "devices",
    device = "spec:shape=Normal,width=1280,height=800,unit=dp,dpi=480"
)
annotation class DevicePreviews

/**
 * Add this multi-preview annotation to a composable to render the composable in various common
 * configurations:
 * - Dark theme
 * - Small and large font size
 * - various device sizes
 *
 * _Note: Combining multi-preview annotations doesn't mean all the different combinations are shown.
 * Instead, each multi-preview annotation acts by its own and renders only its own variants._
 */
@Preview(
    name = "dark theme",
    group = "themes",
    uiMode = UI_MODE_NIGHT_YES
)
@FontScalePreviews
@DevicePreviews
annotation class CompletePreviews