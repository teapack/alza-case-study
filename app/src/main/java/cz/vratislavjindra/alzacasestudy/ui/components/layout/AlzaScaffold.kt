package cz.vratislavjindra.alzacasestudy.ui.components.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import cz.vratislavjindra.alzacasestudy.ui.components.snackbar.AlzaSnackbar
import cz.vratislavjindra.alzacasestudy.ui.components.snackbar.SnackbarData
import cz.vratislavjindra.alzacasestudy.ui.components.top_app_bar.AlzaTopAppBar
import cz.vratislavjindra.alzacasestudy.ui.components.top_app_bar.TopAppBarAction
import cz.vratislavjindra.alzacasestudy.ui.theme.BackgroundDark
import cz.vratislavjindra.alzacasestudy.ui.theme.BackgroundLight
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AlzaScaffold(
    modifier: Modifier,
    snackbarHostState: SnackbarHostState,
    topBarTitle: String,
    upNavigationAction: (() -> Unit)?,
    topAppBarActions: List<TopAppBarAction> = listOf(),
    snackbarFlow: Flow<SnackbarData>? = null,
    contentIsScrolled: Boolean,
    content: @Composable (paddingValues: PaddingValues) -> Unit
) {
    val context = LocalContext.current
    LaunchedEffect(key1 = snackbarHostState) {
        snackbarFlow?.collectLatest { snackbarData ->
            val snackbarMessage = context.getString(snackbarData.messageResId)
            val actionLabel = snackbarData.snackbarAction?.actionLabelResId.let { labelResId ->
                if (labelResId != null) {
                    context.getString(labelResId)
                } else {
                    null
                }
            }
            val result = snackbarHostState.showSnackbar(
                message = snackbarMessage,
                actionLabel = actionLabel
            )
            if (result == SnackbarResult.ActionPerformed) {
                snackbarData.snackbarAction?.action?.let { it() }
            } else if (result == SnackbarResult.Dismissed) {
                snackbarData.onDismiss?.let { it() }
            }
        }
    }
    Scaffold(
        modifier = modifier,
        topBar = {
            val paddingValues = WindowInsets.statusBars.only(
                sides = WindowInsetsSides.Horizontal + WindowInsetsSides.Top
            ).asPaddingValues()
            AlzaTopAppBar(
                title = topBarTitle,
                upNavigationAction = upNavigationAction,
                actions = topAppBarActions,
                paddingValues = paddingValues,
                contentIsScrolled = contentIsScrolled
            )
        },
        snackbarHost = {
            val paddingValues = WindowInsets.navigationBars.only(
                sides = WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom
            ).asPaddingValues()
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.padding(paddingValues = paddingValues),
                snackbar = { AlzaSnackbar(snackbarData = it) }
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