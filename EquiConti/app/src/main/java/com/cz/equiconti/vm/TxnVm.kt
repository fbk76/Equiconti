package com.cz.equiconti.vm

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// Usiamo la stessa entity di Room, con alias
import com.cz.equiconti.data.Txn as TxnEntity

@HiltViewModel
class TxnVm @Inject constructor() : ViewModel() {

    private val _txns = MutableStateFlow<List<TxnEntity>>(emptyList())
    val txns: StateFlow<List<TxnEntity>> = _txns.asStateFlow()

    fun load(ownerId: Long) {
        // Per ora non leggiamo dal DB: lasciamo vuota (o potresti mettere dati finti)
        _txns.value = emptyList()
    }

    fun addTxn(t: TxnEntity) {
        // Aggiungiamo in memoria per vedere subito la riga in lista
        _txns.value = listOf(t) + _txns.value
    }
}
