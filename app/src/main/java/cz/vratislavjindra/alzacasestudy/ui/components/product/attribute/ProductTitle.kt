package cz.vratislavjindra.alzacasestudy.ui.components.product.attribute

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import cz.vratislavjindra.alzacasestudy.ui.components.list.ListItemTitle

@Composable
fun ProductTitle(
    title: String,
    modifier: Modifier = Modifier,
    maxLines: Int = Int.MAX_VALUE,
    small: Boolean
) {
    if (small) {
        ListItemTitle(
            title = title,
            modifier = modifier,
            maxLines = maxLines
        )
    } else {
        Text(
            text = title,
            modifier = modifier,
            overflow = TextOverflow.Ellipsis,
            maxLines = maxLines,
            style = MaterialTheme.typography.titleMedium
        )
    }
}