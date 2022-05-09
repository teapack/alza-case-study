package cz.vratislavjindra.alzacasestudy.ui

import androidx.compose.runtime.Composable
import cz.vratislavjindra.alzacasestudy.ui.theme.AlzaCaseStudyTheme
import cz.vratislavjindra.alzacasestudy.ui.util.WindowInfo

@Composable
fun AlzaApp(windowInfo: WindowInfo) {
    AlzaCaseStudyTheme {
        AlzaNavGraph(windowInfo = windowInfo)
    }
}