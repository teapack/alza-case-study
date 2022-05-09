package cz.vratislavjindra.alzacasestudy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import cz.vratislavjindra.alzacasestudy.ui.AlzaApp
import cz.vratislavjindra.alzacasestudy.ui.util.rememberWindowInfo
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            val windowInfo = rememberWindowInfo()
            AlzaApp(windowInfo = windowInfo)
        }
    }
}