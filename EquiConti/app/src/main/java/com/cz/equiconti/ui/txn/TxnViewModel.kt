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

    // Leggo l'ID del cavallo dalla nav route / SavedStateHandle (deve esserci).
    private val horseId: Long = checkNotNull(savedStateHandle["horseId"]) {
        "Missing 'horseId' in SavedStateHandle"
    }

    /** Stream reattivo dei movimenti di questo cavallo. */
    val txns: StateFlow<List<Txn>> =
        repo.getTxns(horseId)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    /** Inserisce un nuovo movimento. */
    fun addTxn(amountCents: Long, note: String? = null) {
        viewModelScope.launch {
            val txn = Txn(
                horseId = horseId,
                amountCents = amountCents,
                note = note
                // timestamp e id hanno default nel data class
            )
            repo.insert(txn)
        }
    }

    /** Cancella un movimento esistente. */
    fun deleteTxn(txn: Txn) {
        viewModelScope.launch { repo.delete(txn) }
    }
}
