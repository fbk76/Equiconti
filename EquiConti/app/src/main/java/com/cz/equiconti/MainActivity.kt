// app/src/main/java/com/cz/equiconti/MainActivity.kt
package com.cz.equiconti

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.cz.equiconti.ui.NavGraph
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Schema colori con sfondo verde oliva (richiesta tua)
            val olive = Color(0xFF808000)
            val oliveDark = Color(0xFF556B2F)

            val colors = lightColorScheme(
                primary = oliveDark,
                secondary = olive,
                background = olive,
                surface = olive,
                onPrimary = Color.White,
                onSecondary = Color.White,
                onBackground = Color.White,
                onSurface = Color.White,
            )

            MaterialTheme(colorScheme = colors) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // CHIAMA il Composable NavGraph (con le parentesi!)
                    NavGraph()
                }
            }
        }
    }
}
