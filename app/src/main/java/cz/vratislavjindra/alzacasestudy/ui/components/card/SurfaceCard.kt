package cz.vratislavjindra.alzacasestudy.ui.components.card

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cz.vratislavjindra.alzacasestudy.core.utils.CompletePreviews
import cz.vratislavjindra.alzacasestudy.ui.theme.AlzaCaseStudyTheme
import cz.vratislavjindra.alzacasestudy.ui.theme.utils.applyTonalElevation

@Composable
fun SurfaceCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    SurfaceCardSelectable(
        modifier = modifier,
        selected = false,
        onClick = onClick,
        content = content
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun SurfaceCardSelectable(
    modifier: Modifier = Modifier,
    selected: Boolean,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = modifier,
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = if (selected) {
                MaterialTheme.colorScheme.applyTonalElevation(
                    backgroundColor = MaterialTheme.colorScheme.surface,
                    elevation = 3.dp
                )
            } else {
                MaterialTheme.colorScheme.surface
            }
        )
    ) { content() }
}

@Composable
@CompletePreviews
fun SurfaceCardSelectableSelectedPreview() {
    AlzaCaseStudyTheme {
        SurfaceCardSelectable(
            selected = true,
            onClick = {},
            content = {
                Text(
                    text = "Selected card",
                    modifier = Modifier.padding(all = 16.dp)
                )
            }
        )
    }
}



@Composable
@CompletePreviews
fun SurfaceCardSelectableNotSelectedPreview() {
    AlzaCaseStudyTheme {
        SurfaceCardSelectable(
            selected = false,
            onClick = {},
            content = {
                Text(
                    text = "Not selected card",
                    modifier = Modifier.padding(all = 16.dp)
                )
            }
        )
    }
}