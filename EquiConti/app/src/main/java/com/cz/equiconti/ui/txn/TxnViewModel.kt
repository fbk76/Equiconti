package com.cz.equiconti.ui.txn

import androidx.lifecycle.SavedStateHandle
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
    private val repo: Repo,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    // recupero dell’ID cavallo dallo state (navArgs)
    private val horseId: Long = checkNotNull(savedStateHandle["horseId"])

    // lista movimenti per cavallo, ordinati (il Dao già li ordina per timestamp DESC)
    val txns: StateFlow<List<Txn>> =
        repo.getTxns(horseId)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    // inserimento: amountCents può essere >0 (entrata) o <0 (uscita)
    fun addTxn(amountCents: Long, note: String?) {
        viewModelScope.launch {
            repo.insert(
                Txn(
                    horseId = horseId,
                    amountCents = amountCents,
                    note = note
                )
            )
        }
    }

    fun deleteTxn(txn: Txn) {
        viewModelScope.launch { repo.delete(txn) }
    }
}
