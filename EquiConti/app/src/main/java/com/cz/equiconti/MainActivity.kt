package com.cz.equiconti

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import com.cz.equiconti.ui.AppNavGraph

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Se hai un tema, avvolgi così:
            // EquiContiTheme {
            AppNavGraph()
            // }
        }
    }
}
