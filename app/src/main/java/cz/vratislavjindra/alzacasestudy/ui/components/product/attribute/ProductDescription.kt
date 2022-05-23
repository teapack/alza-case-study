package cz.vratislavjindra.alzacasestudy.ui.components.product.attribute

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun ProductDescription(
    description: String,
    short: Boolean,
    modifier: Modifier = Modifier
) {
    Text(
        text = description,
        modifier = modifier,
        overflow = TextOverflow.Ellipsis,
        maxLines = if (short) {
            4
        } else {
            Int.MAX_VALUE
        },
        style = MaterialTheme.typography.bodyMedium
    )
}