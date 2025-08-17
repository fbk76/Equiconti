package com.cz.equiconti.ui.owner

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cz.equiconti.data.Owner
import com.cz.equiconti.data.Repo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OwnerAddScreen(
    onBack: () -> Unit,
    vm: OwnerAddViewModel = hiltViewModel()
) {
    var name by remember { mutableStateOf(TextFieldValue("")) }

    fun doSave() {
        val n = name.text.trim()
        if (n.isNotEmpty()) {
            vm.saveOwner(n, onBack)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nuovo proprietario") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Indietro")
                    }
                },
                actions = {
                    IconButton(onClick = ::doSave, enabled = name.text.isNotBlank()) {
                        Icon(Icons.Filled.Save, contentDescription = "Salva")
                    }
                }
            )
        }
    ) { pad ->
        Column(
            modifier = Modifier
                .padding(pad)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nome") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        }
    }
}

@HiltViewModel
class OwnerAddViewModel @Inject constructor(
    private val repo: Repo
) : ViewModel() {
    fun saveOwner(name: String, onDone: () -> Unit) {
        viewModelScope.launch {
            repo.upsertOwner(Owner(id = 0L, name = name))
            onDone()
        }
    }
}
