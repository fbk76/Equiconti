package com.cz.equiconti.vm

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

import com.cz.equiconti.data.Txn

@HiltViewModel
class TxnVm @Inject constructor() : ViewModel() {

    private val _txns = MutableStateFlow<List<Txn>>(emptyList())
    val txns: StateFlow<List<Txn>> = _txns.asStateFlow()

    /** in futuro potrai caricare dal DB usando ownerId */
    fun load(ownerId: Long) {
        // no-op (placeholder). Lasciato per coerenza con la UI.
    }

    fun addTxn(txn: Txn) {
        _txns.update { it + txn }
    }
}
