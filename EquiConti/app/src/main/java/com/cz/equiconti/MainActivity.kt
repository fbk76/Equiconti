package com.cz.equiconti

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import com.cz.equiconti.ui.nav.AppNavGraph
import com.cz.equiconti.ui.theme.EquiContiTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EquiContiRoot()
        }
    }
}

@Composable
private fun EquiContiRoot() {
    val navController = rememberNavController()
    EquiContiTheme {
        Surface {
            AppNavGraph(navController = navController)
        }
    }
}
