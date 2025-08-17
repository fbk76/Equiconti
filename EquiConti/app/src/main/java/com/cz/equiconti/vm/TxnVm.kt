package com.cz.equiconti.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cz.equiconti.data.Repo
import com.cz.equiconti.data.Txn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class TxnVm @Inject constructor(
    private val repo: Repo
) : ViewModel() {

    /** Flusso delle transazioni di un owner */
    fun txns(ownerId: Long): StateFlow<List<Txn>> =
        repo.getTxns(ownerId)               // <— usa il metodo che aggiungiamo nel Repo (punto 2)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    suspend fun upsert(txn: Txn) = repo.upsertTxn(txn)
    suspend fun delete(txn: Txn) = repo.deleteTxn(txn)
}
