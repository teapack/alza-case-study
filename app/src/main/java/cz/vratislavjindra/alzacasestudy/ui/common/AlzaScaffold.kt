package cz.vratislavjindra.alzacasestudy.ui.common

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import cz.vratislavjindra.alzacasestudy.ui.common.snackbar.SnackbarData
import cz.vratislavjindra.alzacasestudy.ui.common.top_app_bar.AlzaTopAppBar
import cz.vratislavjindra.alzacasestudy.ui.common.top_app_bar.TopAppBarAction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AlzaScaffold(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    topBarTitle: String,
    upNavigationAction: (() -> Unit)? = { navController.navigateUp() },
    topAppBarActions: List<TopAppBarAction> = listOf(),
    snackbarFlow: Flow<SnackbarData>? = null,
    content: @Composable (paddingValues: PaddingValues) -> Unit
) {
    LaunchedEffect(key1 = snackbarHostState) {
        snackbarFlow?.collectLatest { snackbarData ->
            val result = snackbarHostState.showSnackbar(
                message = snackbarData.message,
                actionLabel = snackbarData.snackbarAction?.actionLabel
            )
            if (result == SnackbarResult.ActionPerformed) {
                snackbarData.snackbarAction?.action?.let { it() }
            } else if (result == SnackbarResult.Dismissed) {
                snackbarData.dismissAction?.let { it() }
            }
        }
    }
    Scaffold(
        topBar = {
            val paddingValues = WindowInsets.statusBars.only(
                sides = WindowInsetsSides.Horizontal + WindowInsetsSides.Top
            ).asPaddingValues()
            AlzaTopAppBar(
                title = topBarTitle,
                upNavigationAction = upNavigationAction,
                actions = topAppBarActions,
                paddingValues = paddingValues
            )
        },
        snackbarHost = {
            val paddingValues = WindowInsets.navigationBars.only(
                sides = WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom
            ).asPaddingValues()
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.padding(paddingValues = paddingValues)
            )
        }
    ) { innerPadding ->
        content(innerPadding)
    }
}