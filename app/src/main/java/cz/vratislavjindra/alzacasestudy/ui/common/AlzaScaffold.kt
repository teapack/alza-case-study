package cz.vratislavjindra.alzacasestudy.ui.common

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.navigation.NavController
import cz.vratislavjindra.alzacasestudy.ui.common.top_app_bar.AlzaTopAppBar
import cz.vratislavjindra.alzacasestudy.ui.common.top_app_bar.TopAppBarAction
import kotlinx.coroutines.launch

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AlzaScaffold(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    topBarTitle: String,
    upNavigationAction: (() -> Unit)? = { navController.navigateUp() },
    topAppBarActions: List<TopAppBarAction> = listOf(),
    content: @Composable (paddingValues: PaddingValues) -> Unit
) {
    val scope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            AlzaTopAppBar(
                title = topBarTitle,
                upNavigationAction = upNavigationAction,
                actions = topAppBarActions
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            var clickCount by remember { mutableStateOf(0) }
            ExtendedFloatingActionButton(
                onClick = {
                    // show snackbar as a suspend function
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            "Snackbar # ${++clickCount}"
                        )
                    }
                }
            ) { Text("Show snackbar") }
        }
    ) { innerPadding ->
        content(innerPadding)
    }
}