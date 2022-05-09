package cz.vratislavjindra.alzacasestudy.ui.util

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class WindowInfo(
    val screenWidthSize: WindowSize,
    val screenHeightSize: WindowSize,
    val screenWidth: Dp,
    val screenHeight: Dp
) {

    sealed class WindowSize {

        object Compact : WindowSize()
        object Medium : WindowSize()
        object Expanded : WindowSize()
    }
}

@Composable
fun Activity.rememberWindowInfo(): WindowInfo {
    val configuration = LocalConfiguration.current
    return WindowInfo(
        screenWidthSize = when {
            configuration.screenWidthDp < 600 -> WindowInfo.WindowSize.Compact
            configuration.screenWidthDp < 840 -> WindowInfo.WindowSize.Medium
            else -> WindowInfo.WindowSize.Expanded
        },
        screenHeightSize = when {
            configuration.screenHeightDp < 480 -> WindowInfo.WindowSize.Compact
            configuration.screenHeightDp < 900 -> WindowInfo.WindowSize.Medium
            else -> WindowInfo.WindowSize.Expanded
        },
        screenWidth = configuration.screenWidthDp.dp,
        screenHeight = configuration.screenHeightDp.dp
    )
}