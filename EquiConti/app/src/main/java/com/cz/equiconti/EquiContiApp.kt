package com.cz.equiconti

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.cz.equiconti.ui.nav.AppNavHost

@Composable
fun EquiContiApp() {
    MaterialTheme {
        Surface {
            val nav = rememberNavController()
            AppNavHost(nav)
        }
    }
}
