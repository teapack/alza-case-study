package cz.vratislavjindra.alzacasestudy.ui

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.rememberNavController
import cz.vratislavjindra.alzacasestudy.ui.theme.AlzaCaseStudyTheme

@Composable
fun AlzaCaseStudyApp(widthSizeClass: WindowWidthSizeClass) {
    AlzaCaseStudyTheme {
        val navController = rememberNavController()
        val navigationActions = remember(key1 = navController) {
            AlzaNavigationActions(navController = navController)
        }
        AlzaNavGraph(
            widthSizeClass = widthSizeClass,
            navController = navController,
            navigationActions = navigationActions
        )
    }
}