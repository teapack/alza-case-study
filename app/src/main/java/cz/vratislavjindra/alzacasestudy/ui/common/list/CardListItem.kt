package cz.vratislavjindra.alzacasestudy.ui.common.list

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun CardListItem(
    title: String,
    imageUrl: String?,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = modifier.height(height = 64.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            imageUrl?.let {
                ListItemImage(imageUrl = it, contentDescription = title)
                Spacer(modifier = Modifier.width(width = 16.dp))
            }
            ListItemTitle(
                title = title,
                maxLines = 2,
                smallerTextSize = true
            )
        }
    }
}