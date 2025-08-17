package com.cz.equiconti.ui.txn

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cz.equiconti.data.Repo
import com.cz.equiconti.data.Txn
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class TxnViewModel @Inject constructor(
    private val repo: Repo
) : ViewModel() {

    /**
     * Stream dei movimenti per un dato proprietario.
     * Chi usa questa VM può esporre/collezionare questo StateFlow.
     */
    fun txnsForOwner(ownerId: Long): StateFlow<List<Txn>> =
        repo.getTxnsForOwner(ownerId)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    /** Inserisce/aggiorna un movimento. */
    fun addTxn(txn: Txn) {
        viewModelScope.launch {
            repo.upsertTxn(txn)
        }
    }

    /** Elimina un movimento. */
    fun removeTxn(txn: Txn) {
        viewModelScope.launch {
            repo.deleteTxn(txn)
        }
    }
}
