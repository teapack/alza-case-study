package cz.vratislavjindra.alzacasestudy.ui.components.error

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cz.vratislavjindra.alzacasestudy.core.util.AlzaError

@Composable
fun ErrorFullscreen(
    paddingValues: PaddingValues,
    error: AlzaError
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues = paddingValues),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(id = error.errorMessageResId),
            modifier = Modifier.padding(all = 16.dp),
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.titleLarge
        )
    }
}