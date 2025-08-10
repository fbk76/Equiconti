package com.cz.equiconti.vm

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.cz.equiconti.data.Txn

@HiltViewModel
class TxnVm @Inject constructor() : ViewModel() {

    private val _txns = MutableStateFlow<List<Txn>>(emptyList())
    val txns: StateFlow<List<Txn>> = _txns.asStateFlow()

    // In futuro: raccogli da txnDao.listByOwner(ownerId)
    fun load(ownerId: Long) {
        // no-op per ora: lasciamo la lista com'è
    }

    // In futuro: txnDao.insert(txn)
    fun addTxn(txn: Txn) {
        // Aggiorno lo stato locale così la UI vede subito il nuovo record
        _txns.value = listOf(txn.copy(txnId = System.nanoTime())) + _txns.value
    }
}
