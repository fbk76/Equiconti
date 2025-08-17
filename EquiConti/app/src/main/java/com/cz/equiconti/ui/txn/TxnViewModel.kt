package com.cz.equiconti.ui.txn

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cz.equiconti.data.Repo
import com.cz.equiconti.data.Txn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TxnViewModel @Inject constructor(
    private val repo: Repo
) : ViewModel() {

    fun txnsForOwner(ownerId: Long): StateFlow<List<Txn>> =
        repo.getTxns(ownerId)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun upsert(txn: Txn) = viewModelScope.launch { repo.upsertTxn(txn) }

    fun delete(txn: Txn) = viewModelScope.launch { repo.deleteTxn(txn) }
}
