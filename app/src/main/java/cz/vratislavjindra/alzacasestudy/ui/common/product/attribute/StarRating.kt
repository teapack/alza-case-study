package cz.vratislavjindra.alzacasestudy.ui.common.product.attribute

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cz.vratislavjindra.alzacasestudy.R
import cz.vratislavjindra.alzacasestudy.ui.theme.StarFilledDark
import cz.vratislavjindra.alzacasestudy.ui.theme.StarFilledLight
import cz.vratislavjindra.alzacasestudy.ui.theme.StarNotFilledDark
import cz.vratislavjindra.alzacasestudy.ui.theme.StarNotFilledLight

@Composable
fun StarRating(
    rating: Float,
    small: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.width(
            width = if (small) {
                96.dp
            } else {
                128.dp
            }
        )
    ) {
        Star(
            filled = rating >= 0.5f,
            scope = this
        )
        Star(
            filled = rating >= 1.5f,
            scope = this
        )
        Star(
            filled = rating >= 2.5f,
            scope = this
        )
        Star(
            filled = rating >= 3.5f,
            scope = this
        )
        Star(
            filled = rating >= 4.5f,
            scope = this
        )
    }
}

@Composable
private fun Star(filled: Boolean, scope: RowScope) {
    scope.apply {
        Icon(
            imageVector = Icons.Rounded.Star,
            contentDescription = stringResource(id = R.string.content_description_product_rating),
            modifier = Modifier.weight(weight = 1f),
            tint = if (filled) {
                if (isSystemInDarkTheme()) {
                    StarFilledDark
                } else {
                    StarFilledLight
                }
            } else {
                if (isSystemInDarkTheme()) {
                    StarNotFilledDark
                } else {
                    StarNotFilledLight
                }
            }
        )
    }
}