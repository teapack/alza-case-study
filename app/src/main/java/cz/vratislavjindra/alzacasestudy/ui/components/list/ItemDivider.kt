package cz.vratislavjindra.alzacasestudy.ui.components.list

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import cz.vratislavjindra.alzacasestudy.ui.theme.DividerDark
import cz.vratislavjindra.alzacasestudy.ui.theme.DividerLight

@Composable
fun ItemDivider(onCard: Boolean = false) {
    Divider(
        color = if (onCard) {
            MaterialTheme.colorScheme.surface.copy(alpha = 0.75f)
        } else {
            if (isSystemInDarkTheme()) {
                DividerDark
            } else {
                DividerLight
            }
        },
        thickness = 0.5.dp
    )
}