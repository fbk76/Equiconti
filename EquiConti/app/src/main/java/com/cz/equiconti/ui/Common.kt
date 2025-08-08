package com.cz.equiconti.ui

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EquiSmallTopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    navigationIcon: (@Composable () -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {}
) {
    SmallTopAppBar(
        modifier = modifier,
        title = { Text(title) },
        navigationIcon = { navigationIcon?.invoke() },
        actions = actions,
        colors = TopAppBarDefaults.smallTopAppBarColors()
    )
}

/**
 * Se in questo file avevi chiamate a composable “sciolte” (fuori da funzioni),
 * ora spostale DENTRO funzioni annotate @Composable, come sopra.
 */
