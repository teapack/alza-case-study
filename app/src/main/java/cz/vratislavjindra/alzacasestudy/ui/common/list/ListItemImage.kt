package cz.vratislavjindra.alzacasestudy.ui.common.list

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun ListItemImage(imageUrl: String, contentDescription: String) {
    AsyncImage(
        model = ImageRequest.Builder(context = LocalContext.current)
            .data(data = imageUrl)
            .crossfade(enable = true)
            .build(),
        contentDescription = contentDescription,
        modifier = Modifier.size(size = 24.dp)
    )
}