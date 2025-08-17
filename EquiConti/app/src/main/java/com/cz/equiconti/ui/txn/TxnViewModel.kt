package com.cz.equiconti.ui.txn

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cz.equiconti.data.Repo
import com.cz.equiconti.data.Txn
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@HiltViewModel
class TxnViewModel @Inject constructor(
    private val repo: Repo
) : ViewModel() {

    /** Lista movimenti per proprietario (tutti i cavalli di quell'owner). */
    fun getTxnsForOwner(ownerId: Long): Flow<List<Txn>> = repo.getTxns(ownerId)

    /** Inserisce un nuovo movimento sul cavallo selezionato. */
    fun addTxn(
        horseId: Long,
        amountCents: Long,
        notes: String? = null,
        onDone: () -> Unit = {}
    ) {
        viewModelScope.launch {
            repo.upsertTxn(
                Txn(
                    id = 0L,
                    horseId = horseId,
                    amountCents = amountCents,
                    notes = notes
                )
            )
            onDone()
        }
    }

    /** (opzionale) elimina un movimento. */
    fun deleteTxn(txn: Txn, onDone: () -> Unit = {}) {
        viewModelScope.launch {
            repo.deleteTxn(txn)
            onDone()
        }
    }
}
