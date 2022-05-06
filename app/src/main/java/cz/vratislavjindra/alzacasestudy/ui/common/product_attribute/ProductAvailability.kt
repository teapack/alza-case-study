package cz.vratislavjindra.alzacasestudy.ui.common.product_attribute

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cz.vratislavjindra.alzacasestudy.ui.theme.ProductAvailableDark
import cz.vratislavjindra.alzacasestudy.ui.theme.ProductAvailableLight
import cz.vratislavjindra.alzacasestudy.ui.theme.ProductUnavailableDark
import cz.vratislavjindra.alzacasestudy.ui.theme.ProductUnavailableLight

@Composable
fun ProductAvailability(
    availability: String,
    available: Boolean,
    small: Boolean,
    modifier: Modifier = Modifier
) {
    Text(
        text = availability,
        modifier = modifier,
        maxLines = 1,
        style = if (small) {
            MaterialTheme.typography.titleSmall
        } else {
            MaterialTheme.typography.titleMedium
        },
        color = if (available) {
            if (isSystemInDarkTheme()) {
                ProductAvailableDark
            } else {
                ProductAvailableLight
            }
        } else {
            if (isSystemInDarkTheme()) {
                ProductUnavailableDark
            } else {
                ProductUnavailableLight
            }
        }
    )
}