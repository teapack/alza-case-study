package cz.vratislavjindra.alzacasestudy.ui.components.loading

import androidx.compose.foundation.layout.*
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cz.vratislavjindra.alzacasestudy.R

@Composable
fun LoadingIndicatorFullscreen(
    modifier: Modifier,
    paddingValues: PaddingValues
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(paddingValues = paddingValues),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LinearProgressIndicator(modifier = Modifier.fillMaxWidth(fraction = 0.5f))
        Text(
            text = stringResource(id = R.string.title_loading),
            modifier = Modifier.padding(all = 16.dp),
            style = MaterialTheme.typography.titleLarge
        )
    }
}