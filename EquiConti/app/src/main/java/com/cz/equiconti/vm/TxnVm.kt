package com.cz.equiconti.vm

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cz.equiconti.data.EquiDb
import com.cz.equiconti.data.Txn
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class TxnVm @Inject constructor(
    @ApplicationContext context: Context
) : ViewModel() {

    private val db = EquiDb.get(context)

    private val _txns = MutableStateFlow<List<Txn>>(emptyList())
    val txns: StateFlow<List<Txn>> = _txns.asStateFlow()

    fun load(ownerId: Long) {
        viewModelScope.launch {
            db.txnDao().listByOwner(ownerId).collect { _txns.value = it }
        }
    }

    fun addTxn(txn: Txn) {
        viewModelScope.launch {
            db.txnDao().insert(txn)
        }
    }
}
