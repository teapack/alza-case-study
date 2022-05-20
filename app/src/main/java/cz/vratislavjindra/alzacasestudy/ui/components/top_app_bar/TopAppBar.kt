package cz.vratislavjindra.alzacasestudy.ui.components.top_app_bar

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import cz.vratislavjindra.alzacasestudy.R
import cz.vratislavjindra.alzacasestudy.ui.theme.BackgroundDark
import cz.vratislavjindra.alzacasestudy.ui.theme.BackgroundLight
import cz.vratislavjindra.alzacasestudy.ui.theme.utils.applyTonalElevation

@Composable
fun AlzaTopAppBar(
    title: String,
    upNavigationAction: (() -> Unit)?,
    actions: List<TopAppBarAction>,
    paddingValues: PaddingValues,
    contentIsScrolled: Boolean
) {
    val systemUiController = rememberSystemUiController()
    val systemIsInDarkTheme = isSystemInDarkTheme()
    SideEffect {
        systemUiController.setStatusBarColor(
            color = Color.Transparent,
            darkIcons = !systemIsInDarkTheme
        )
    }
    SmallTopAppBar(
        title = {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onSurface,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = MaterialTheme.typography.titleLarge
            )
        },
        modifier = Modifier
            .background(
                color = animateColorAsState(
                    targetValue = if (contentIsScrolled) {
                        MaterialTheme.colorScheme.applyTonalElevation(
                            backgroundColor = MaterialTheme.colorScheme.surface,
                            elevation = 3.dp
                        ).copy(alpha = 0.95f)
                    } else {
                        if (isSystemInDarkTheme()) {
                            BackgroundDark
                        } else {
                            BackgroundLight
                        }
                    },
                    animationSpec = tween(
                        durationMillis = 1_000,
                        easing = LinearOutSlowInEasing
                    )
                ).value
            )
            .padding(paddingValues = paddingValues),
        navigationIcon = {
            if (upNavigationAction != null) {
                IconButton(onClick = upNavigationAction) {
                    Icon(
                        imageVector = Icons.Rounded.ArrowBack,
                        contentDescription = stringResource(
                            id = R.string.content_description_button_navigate_up
                        ),
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        },
        actions = {
            LazyRow {
                items(items = actions) { action ->
                    IconButton(onClick = action.onClick) {
                        Icon(
                            imageVector = action.icon,
                            contentDescription = action.contentDescription,
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color.Transparent)
    )
}