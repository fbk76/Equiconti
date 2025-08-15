package com.cz.equiconti

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.navigation.compose.rememberNavController
import com.cz.equiconti.ui.NavGraph()
import com.cz.equiconti.ui.theme.EquicontiTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EquicontiTheme {
                Surface {
                    val nav = rememberNavController()
                    NavGraph(navController = nav)
                }
            }
        }
    }
}
