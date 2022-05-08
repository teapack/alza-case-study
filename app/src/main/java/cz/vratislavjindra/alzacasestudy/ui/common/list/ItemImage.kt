package cz.vratislavjindra.alzacasestudy.ui.common.list

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest

@Composable
fun ItemImage(
    imageUrl: String,
    contentDescription: String,
    modifier: Modifier = Modifier
) {
    val imageLoaderBuilder = ImageLoader.Builder(context = LocalContext.current)
    if (imageUrl.endsWith(suffix = ".svg")) {
        imageLoaderBuilder.components { add(factory = SvgDecoder.Factory()) }
    }
    val imageLoader = imageLoaderBuilder.build()
    AsyncImage(
        model = ImageRequest.Builder(context = LocalContext.current)
            .data(data = imageUrl)
            .crossfade(enable = true)
            .build(),
        contentDescription = contentDescription,
        imageLoader = imageLoader,
        modifier = modifier
    )
}