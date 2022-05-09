package cz.vratislavjindra.alzacasestudy.ui.components.layout

import androidx.compose.foundation.layout.*
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cz.vratislavjindra.alzacasestudy.ui.components.AlzaScaffold
import cz.vratislavjindra.alzacasestudy.ui.components.top_app_bar.TopAppBarAction

@Composable
fun TwoPane(
    title: String,
    upNavigationAction: (() -> Unit)?,
    topAppBarActions: List<TopAppBarAction>,
    leftPaneContent: @Composable (modifier: Modifier, paddingValues: PaddingValues) -> Unit,
    rightPaneContent: @Composable (modifier: Modifier, paddingValues: PaddingValues) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    AlzaScaffold(
        modifier = Modifier,
        snackbarHostState = snackbarHostState,
        topBarTitle = title,
        upNavigationAction = upNavigationAction,
        topAppBarActions = topAppBarActions,
//        snackbarFlow = viewModel.snackbarDataFlow
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            Row {
                leftPaneContent(
                    modifier = Modifier.weight(weight = 1f),
                    paddingValues = paddingValues
                )
                Surface(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(width = 8.dp)
                        .padding(paddingValues = paddingValues),
                    shadowElevation = 2.dp
                ) {}
                rightPaneContent(
                    modifier = Modifier.weight(weight = 2f),
                    paddingValues = paddingValues
                )
            }
        }
    }
}