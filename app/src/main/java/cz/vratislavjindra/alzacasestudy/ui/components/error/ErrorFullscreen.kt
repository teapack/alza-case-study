package cz.vratislavjindra.alzacasestudy.ui.components.error

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ErrorOutline
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cz.vratislavjindra.alzacasestudy.R
import cz.vratislavjindra.alzacasestudy.core.utils.CompletePreviews
import cz.vratislavjindra.alzacasestudy.core.utils.ErrorMessage
import cz.vratislavjindra.alzacasestudy.ui.theme.AlzaCaseStudyTheme

@Composable
fun ErrorFullscreen(
    paddingValues: PaddingValues,
    errorMessage: ErrorMessage,
    onTryAgainButtonClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues = paddingValues),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Rounded.ErrorOutline,
            contentDescription = stringResource(
                id = R.string.content_description_error_illustration
            ),
            modifier = Modifier
                .size(size = 192.dp)
                .padding(all = 16.dp),
            tint = MaterialTheme.colorScheme.error
        )
        Text(
            text = stringResource(id = errorMessage.messageId),
            modifier = Modifier.padding(all = 16.dp),
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center
        )
        Button(
            onClick = onTryAgainButtonClick,
            modifier = Modifier.padding(all = 16.dp)
        ) { Text(text = stringResource(id = R.string.button_try_again)) }
    }
}

@Composable
@CompletePreviews
fun ErrorFullscreenPreview() {
    AlzaCaseStudyTheme {
        ErrorFullscreen(
            paddingValues = PaddingValues(),
            errorMessage = ErrorMessage(messageId = R.string.error_load_categories),
            onTryAgainButtonClick = {}
        )
    }
}