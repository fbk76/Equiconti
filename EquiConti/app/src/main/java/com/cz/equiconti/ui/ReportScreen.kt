package com.cz.equiconti.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.cz.equiconti.util.formatCurrency
import com.cz.equiconti.vm.ReportVm
import java.time.LocalDate

@Composable
fun ReportScreen(nav: NavController, ownerId: Long, vm: ReportVm = hiltViewModel()) {
    var from by remember { mutableStateOf(LocalDate.now().withDayOfMonth(1).toString()) }
    var to by remember { mutableStateOf(LocalDate.now().toString()) }
    val report by vm.report.collectAsState()

    Scaffold(topBar = { TopBar("Report") { nav.popBackStack() } }) { pad ->
        Column(Modifier.padding(pad).padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(from, { from = it }, label = { Text("Dal (yyyy-MM-dd)") }, modifier = Modifier.weight(1f))
                OutlinedTextField(to, { to = it }, label = { Text("Al (yyyy-MM-dd)") }, modifier = Modifier.weight(1f))
                Button(onClick = { vm.load(ownerId, from, to) }) { Text("Aggiorna") }
            }
            Divider()
            Text("Saldo iniziale: " + formatCurrency(report?.startBalanceCents ?: 0))
            LazyColumn {
                var running = report?.startBalanceCents ?: 0L
                items(report?.rows ?: emptyList()) { r ->
                    running += r.incomeCents - r.expenseCents
                    ListItem(
                        headlineContent = { Text("${r.date} • ${r.operation}") },
                        supportingContent = {
                            Text("Entrate: " + formatCurrency(r.incomeCents) + "  Uscite: " + formatCurrency(r.expenseCents))
                        },
                        trailingContent = { Text(formatCurrency(running)) }
                    )
                    Divider()
                }
            }
        }
    }
}
