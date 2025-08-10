package com.cz.equiconti.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.cz.equiconti.util.formatCurrency
import com.cz.equiconti.vm.ReportVm
import java.time.LocalDate

@Composable
fun ReportScreen(
    nav: NavController,
    ownerId: Long,
    vm: ReportVm = hiltViewModel()
) {
    LaunchedEffect(ownerId) { vm.load(ownerId) }
    val txns by vm.txns.collectAsState()

    Scaffold(
        topBar = { CenterAlignedTopAppBar(title = { Text("Report") }) }
    ) { pad ->
        Column(Modifier.padding(pad)) {
            LazyColumn {
                var balance = 0L
                items(txns) { t ->
                    balance += t.incomeCents - t.expenseCents
                    ListItem(
                        headlineContent = { Text("${millisToYmd(t.dateMillis)} • ${t.operation}") },
                        supportingContent = {
                            Text("Entrate: ${formatCurrency(t.incomeCents)}  Uscite: ${formatCurrency(t.expenseCents)}")
                        },
                        trailingContent = { Text(formatCurrency(balance)) }
                    )
                    Divider()
                }
            }
        }
    }
}

private fun millisToYmd(millis: Long): String =
    LocalDate.ofEpochDay(millis / 86_400_000L).toString()
