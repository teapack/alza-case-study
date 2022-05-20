package cz.vratislavjindra.alzacasestudy.ui.components.layout

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cz.vratislavjindra.alzacasestudy.core.utils.CompletePreviews
import cz.vratislavjindra.alzacasestudy.ui.theme.AlzaCaseStudyTheme

@Composable
fun EmptyContentPlaceholder(
    modifier: Modifier,
    paddingValues: PaddingValues,
    text: String
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(paddingValues = paddingValues),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(all = 16.dp),
            style = MaterialTheme.typography.titleLarge
        )
    }
}

@Composable
fun EmptyContentPlaceholderWithButton(
    modifier: Modifier,
    paddingValues: PaddingValues,
    text: String,
    buttonText: String,
    buttonAction: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(paddingValues = paddingValues),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(all = 16.dp),
            style = MaterialTheme.typography.titleLarge
        )
        Button(onClick = buttonAction) {
            Text(text = buttonText)
        }
    }
}

@CompletePreviews
@Composable
fun EmptyContentPlaceholderPreview() {
    AlzaCaseStudyTheme {
        EmptyContentPlaceholder(
            modifier = Modifier,
            paddingValues = PaddingValues(),
            text = "No products"
        )
    }
}