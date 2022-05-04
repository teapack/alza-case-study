package cz.vratislavjindra.alzacasestudy.ui.common.top_app_bar

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import cz.vratislavjindra.alzacasestudy.R

@Composable
fun AlzaTopAppBar(
    title: String,
    upNavigationAction: (() -> Unit)?,
    actions: List<TopAppBarAction>
) {
    MediumTopAppBar(
        title = {
            Text(
                text = title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleLarge
            )
        },
        modifier = Modifier.fillMaxWidth(),
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
            actions.forEach { action ->
                IconButton(onClick = action.onClick) {
                    Icon(
                        imageVector = action.icon,
                        contentDescription = action.contentDescription
                    )
                }
            }
//            Box {
//                LazyRow {
//                    items(items = actions) { item ->
//                        IconButton(onClick = item.onClick) {
//                            Icon(
//                                imageVector = item.icon,
//                                contentDescription = item.contentDescription
//                            )
//                        }
//                    }
//                }
//            }
        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    )
}