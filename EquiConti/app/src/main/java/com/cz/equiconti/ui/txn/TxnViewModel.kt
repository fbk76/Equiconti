package com.cz.equiconti.ui.txn

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cz.equiconti.data.Horse
import com.cz.equiconti.data.Repo
import com.cz.equiconti.data.Txn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TxnViewModel @Inject constructor(
    private val repo: Repo
) : ViewModel() {

    /** Stream dei movimenti per proprietario */
    fun getTxnsForOwner(ownerId: Long): Flow<List<Txn>> = repo.getTxns(ownerId)

    /** Stream dei cavalli del proprietario (utile per AddTxnScreen) */
    fun getHorses(ownerId: Long): Flow<List<Horse>> = repo.getHorses(ownerId)

    /** Crea/aggiorna un movimento */
    fun addTxn(horseId: Long, amountCents: Long, notes: String?) {
        viewModelScope.launch {
            repo.upsertTxn(
                Txn(
                    id = 0L,
                    horseId = horseId,
                    amountCents = amountCents,
                    notes = notes
                )
            )
        }
    }
}
