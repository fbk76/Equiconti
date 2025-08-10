package com.cz.equiconti.vm

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

import com.cz.equiconti.data.Owner
import com.cz.equiconti.data.Txn

@HiltViewModel
class OwnersVm @Inject constructor() : ViewModel() {

    private val _owners = MutableStateFlow<List<Owner>>(listOf(
        Owner(id = 1L, firstName = "Fulvia", lastName = "Bolzan", phone = "123456789")
    ))
    val owners: StateFlow<List<Owner>> = _owners.asStateFlow()

    fun refresh() { /* no-op, placeholder */ }
}

@HiltViewModel
class OwnerDetailVm @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    val ownerId: Long = savedStateHandle["ownerId"] ?: 0L
    // Stato/logica aggiuntiva quando servirà
}

@HiltViewModel
class ReportVm @Inject constructor() : ViewModel() {
    // Semplice stato locale dei movimenti, così ReportScreen compila e gira.
    private val _txns = MutableStateFlow<List<Txn>>(emptyList())
    val txns: StateFlow<List<Txn>> = _txns.asStateFlow()

    fun load(ownerId: Long) {
        // In futuro potrai leggere dal DB usando ownerId
    }
}
