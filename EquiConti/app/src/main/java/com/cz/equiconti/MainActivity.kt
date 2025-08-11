package com.cz.equiconti

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.cz.equiconti.ui.nav.AppNavGraph
import com.cz.equiconti.ui.theme.EquicontiTheme   // <-- importa il tuo tema qui

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EquicontiTheme {
                val navController = rememberNavController()
                AppNavGraph(navController)       // dentro setContent (contesto @Composable)
            }
        }
    }
}
