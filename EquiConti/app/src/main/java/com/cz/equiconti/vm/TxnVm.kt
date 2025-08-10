package com.cz.equiconti.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cz.equiconti.data.EquiDb
import com.cz.equiconti.data.Txn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TxnVm @Inject constructor(
    private val db: EquiDb
) : ViewModel() {

    private val _txns = MutableStateFlow<List<Txn>>(emptyList())
    val txns: StateFlow<List<Txn>> = _txns.asStateFlow()

    private var currentOwnerId: Long = 0L

    fun load(ownerId: Long) {
        currentOwnerId = ownerId
        viewModelScope.launch {
            db.txnDao().listByOwner(ownerId).collect { _txns.value = it }
        }
    }

    fun addTxn(t: Txn) {
        viewModelScope.launch {
            db.txnDao().insert(t)
            // listByOwner() è un Flow: si aggiorna da solo
        }
    }
}
