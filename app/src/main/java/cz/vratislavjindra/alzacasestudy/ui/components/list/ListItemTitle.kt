package cz.vratislavjindra.alzacasestudy.ui.components.list

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun ListItemTitle(
    title: String,
    modifier: Modifier = Modifier,
    maxLines: Int = 1
) {
    Text(
        text = title,
        modifier = modifier,
        overflow = TextOverflow.Ellipsis,
        maxLines = maxLines,
        style = MaterialTheme.typography.titleSmall
    )
}