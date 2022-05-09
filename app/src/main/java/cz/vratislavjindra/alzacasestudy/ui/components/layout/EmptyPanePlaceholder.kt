package cz.vratislavjindra.alzacasestudy.ui.components.layout

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun EmptyPanePlaceholder(
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