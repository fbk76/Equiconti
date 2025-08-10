package com.cz.equiconti.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.cz.equiconti.util.formatCurrency
import com.cz.equiconti.vm.ReportVm
import java.time.Instant
import java.time.ZoneId

@Composable
fun ReportScreen(nav: NavController, ownerId: Long, vm: ReportVm = hiltViewModel()) {
    LaunchedEffect(ownerId) { vm.load(ownerId) }
    val report by vm.report.collectAsState()

    Scaffold(
        topBar = { TopBar("Report") { nav.popBackStack() } }
    ) { pad ->
        Column(Modifier.padding(pad).padding(12.dp)) {
            Text("Saldo iniziale: ${formatCurrency(report.startBalanceCents)}")
            Spacer(Modifier.height(8.dp))
            LazyColumn {
                var running = report.startBalanceCents
                items(report.rows) { r ->
                    running += r.incomeCents - r.expenseCents
                    ListItem(
                        headlineContent = {
                            Text("${r.dateMillis.toYmd()} • ${r.operation}")
                        },
                        supportingContent = {
                            Text("Entrate: ${formatCurrency(r.incomeCents)}  Uscite: ${formatCurrency(r.expenseCents)}")
                        },
                        trailingContent = { Text(formatCurrency(running)) }
                    )
                    Divider()
                }
            }
        }
    }
}

private fun Long.toYmd(): String =
    Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault()).toLocalDate().toString()
