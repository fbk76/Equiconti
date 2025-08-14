package com.cz.equiconti.ui.txn

// ——— Compose base ———
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

// ——— Material / UI ———
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.FloatingActionButton

// ——— Layout ———
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// ✅ Import mancanti che causavano il fallimento
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.ImeAction

// ——— (Se usi Navigation) ———
import androidx.navigation.NavController

// ——— Se nel file fai anteprime ———
import androidx.compose.ui.tooling.preview.Preview

// ─────────────────────────────────────────────────────────────────────────────
// DA QUI IN GIÙ lascia il tuo contenuto invariato (fun TxnScreen(..) ecc.)
// Ho incluso alcuni import “Material3” e layout comuni. Se qualcuno non serve,
// il compilatore lo segnalerà come “unused import”: puoi rimuoverlo senza problemi.
// ─────────────────────────────────────────────────────────────────────────────
