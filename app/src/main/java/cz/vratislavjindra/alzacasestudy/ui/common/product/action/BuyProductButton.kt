package cz.vratislavjindra.alzacasestudy.ui.common.product.action

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cz.vratislavjindra.alzacasestudy.R

@Composable
fun BuyProductAction(onClick: () -> Unit) {
    TextButton(onClick = onClick) {
        Icon(
            imageVector = Icons.Rounded.ShoppingCart,
            contentDescription = stringResource(
                id = R.string.content_description_button_buy
            )
        )
        Spacer(modifier = Modifier.width(width = 8.dp))
        Text(text = stringResource(id = R.string.button_buy))
    }
}