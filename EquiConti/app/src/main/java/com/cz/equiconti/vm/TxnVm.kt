package com.cz.equiconti.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cz.equiconti.data.Txn
import com.cz.equiconti.data.TxnDao
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class TxnVm @Inject constructor(
    private val txnDao: TxnDao
) : ViewModel() {

    private val _txns = MutableStateFlow<List<Txn>>(emptyList())
    val txns: StateFlow<List<Txn>> = _txns

    fun load(ownerId: Long) {
        viewModelScope.launch {
            txnDao.observeByOwner(ownerId).collect { _txns.value = it }
        }
    }

    fun addTxn(txn: Txn) {
        viewModelScope.launch { txnDao.insert(txn) }
    }
}
