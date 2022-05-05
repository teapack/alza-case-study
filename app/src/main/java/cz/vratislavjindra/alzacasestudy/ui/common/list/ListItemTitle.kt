package cz.vratislavjindra.alzacasestudy.ui.common.list

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun ListItemTitle(
    title: String,
    maxLines: Int = 1,
    smallerTextSize: Boolean = false
) {
    Text(
        text = title,
        overflow = TextOverflow.Ellipsis,
        maxLines = maxLines,
        style = if (smallerTextSize) {
            MaterialTheme.typography.bodyMedium
        } else {
            MaterialTheme.typography.bodyLarge
        }
    )
}