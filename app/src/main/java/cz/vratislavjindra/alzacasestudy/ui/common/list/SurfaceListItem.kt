package cz.vratislavjindra.alzacasestudy.ui.common.list

import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun SurfaceListItem(
    title: String,
    imageUrl: String?,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(all = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            imageUrl?.let {
                ItemImage(imageUrl = it, contentDescription = title)
                Spacer(modifier = Modifier.width(width = 16.dp))
            }
            ListItemTitle(title = title)
        }
    }
}