package cz.vratislavjindra.alzacasestudy.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import cz.vratislavjindra.alzacasestudy.ui.common.snackbar.SnackbarData
import cz.vratislavjindra.alzacasestudy.ui.common.top_app_bar.AlzaTopAppBar
import cz.vratislavjindra.alzacasestudy.ui.common.top_app_bar.TopAppBarAction
import cz.vratislavjindra.alzacasestudy.ui.theme.BackgroundDark
import cz.vratislavjindra.alzacasestudy.ui.theme.BackgroundLight
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
    val context = LocalContext.current
    LaunchedEffect(key1 = snackbarHostState) {
        snackbarFlow?.collectLatest { snackbarData ->
            val snackbarMessage = when (snackbarData) {
                is SnackbarData.ErrorSnackbarData ->
                    snackbarData.message ?: context.getString(snackbarData.error.errorMessageResId)
                is SnackbarData.MessageSnackbarData -> snackbarData.message
            }
            val result = snackbarHostState.showSnackbar(
                message = snackbarMessage,
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = if (isSystemInDarkTheme()) {
                        BackgroundDark
                    } else {
                        BackgroundLight
                    }
                )
        ) { content(innerPadding) }
    }
}