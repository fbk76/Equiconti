package com.cz.equiconti.ui.home

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.compose.rememberNavController
import com.cz.equiconti.ui.NavGraph

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScaffold() {
    val navController = rememberNavController()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Equiconti",
                        style = MaterialTheme.typography.titleLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            )
        }
    ) { _ ->
        // Tutta la navigazione (owners, owner/{id}, addHorse, txns) è dentro NavGraph
        NavGraph(navController = navController)
    }
}
