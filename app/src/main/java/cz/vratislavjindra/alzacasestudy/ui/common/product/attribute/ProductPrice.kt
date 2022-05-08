package cz.vratislavjindra.alzacasestudy.ui.common.product.attribute

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cz.vratislavjindra.alzacasestudy.ui.theme.ProductPriceDark
import cz.vratislavjindra.alzacasestudy.ui.theme.ProductPriceLight

@Composable
fun ProductPrice(
    price: String,
    small: Boolean,
    modifier: Modifier = Modifier
) {
    Text(
        text = price,
        modifier = modifier,
        maxLines = 1,
        style = if (small) {
            MaterialTheme.typography.titleSmall
        } else {
            MaterialTheme.typography.titleMedium
        },
        color = if (isSystemInDarkTheme()) {
            ProductPriceDark
        } else {
            ProductPriceLight
        }
    )
}