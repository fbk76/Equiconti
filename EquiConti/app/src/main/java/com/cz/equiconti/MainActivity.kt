package com.cz.equiconti

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import com.cz.equiconti.ui.AppNavGraph
import com.cz.equiconti.ui.theme.EquicontiTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // ATTENZIONE: il nome del tema deve esistere in ui/theme
            EquicontiTheme {
                AppNavGraph()
            }
        }
    }
}
