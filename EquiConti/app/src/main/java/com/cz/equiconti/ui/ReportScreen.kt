package com.cz.equiconti.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.cz.equiconti.data.Repo
import com.cz.equiconti.util.formatCurrency
import java.time.Instant
import java.time.ZoneId
import java.time.LocalDate

@Composable
fun ReportScreen(nav: NavController, ownerId: Long) {
    val repo = remember(nav.context) { Repo.from(nav.context) }

    var from by remember { mutableStateOf(LocalDate.now().withDayOfMonth(1)) }
    var to by remember { mutableStateOf(LocalDate.now()) }

    var startBalance by remember { mutableStateOf(0L) }
    var rows by remember { mutableStateOf(emptyList<ReportRowUi>()) }

    LaunchedEffect(ownerId, from, to) {
        val r = repo.report(ownerId, from, to)
        startBalance = r.startBalanceCents
        rows = r.rows.map {
            ReportRowUi(
                date = millisToYmd(it.dateMillis),
                operation = it.operation,
                incomeCents = it.incomeCents,
                expenseCents = it.expenseCents
            )
        }
    }

    Scaffold(
        topBar = { CenterAlignedTopAppBar(title = { Text("Report") }) }
    ) { pad ->
        Column(Modifier.padding(pad).padding(16.dp)) {
            Text("Saldo iniziale: ${formatCurrency(startBalance)}", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            LazyColumn {
                items(rows) { r ->
                    ListItem(
                        headlineContent = { Text("${r.date} • ${r.operation}") },
                        supportingContent = {
                            Text("Entrate: ${formatCurrency(r.incomeCents)}  Uscite: ${formatCurrency(r.expenseCents)}")
                        }
                    )
                    Divider()
                }
            }
        }
    }
}

private data class ReportRowUi(
    val date: String,
    val operation: String,
    val incomeCents: Long,
    val expenseCents: Long
)

private fun millisToYmd(millis: Long): String =
    Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDate().toString()
