package cz.vratislavjindra.alzacasestudy.ui.common.top_app_bar

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import cz.vratislavjindra.alzacasestudy.R
import cz.vratislavjindra.alzacasestudy.ui.theme.TOP_APP_BAR_ALPHA

@Composable
fun AlzaTopAppBar(
    title: String,
    upNavigationAction: (() -> Unit)?,
    actions: List<TopAppBarAction>,
    paddingValues: PaddingValues
) {
    SmallTopAppBar(
        title = {
            Text(
                text = title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleLarge
            )
        },
        modifier = Modifier.padding(paddingValues = paddingValues),
        navigationIcon = {
            if (upNavigationAction != null) {
                IconButton(onClick = upNavigationAction) {
                    Icon(
                        imageVector = Icons.Rounded.ArrowBack,
                        contentDescription = stringResource(
                            id = R.string.content_description_button_navigate_up
                        )
                    )
                }
            }
        },
        actions = {
            LazyRow {
                items(items = actions) { action ->
                    IconButton(onClick = action.onClick) {
                        val rotationModifier = if (action.rotateIcon) {
                            val infiniteTransition = rememberInfiniteTransition()
                            val rotationAngle by infiniteTransition.animateFloat(
                                initialValue = 0F,
                                targetValue = 360F,
                                animationSpec = infiniteRepeatable(
                                    animation = tween(durationMillis = 1_500, easing = LinearEasing)
                                )
                            )
                            Modifier.rotate(degrees = rotationAngle)
                        } else {
                            Modifier
                        }
                        Icon(
                            imageVector = action.icon,
                            contentDescription = action.contentDescription,
                            modifier = rotationModifier
                        )
                    }
                }
            }
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = TOP_APP_BAR_ALPHA)
        )
    )
}