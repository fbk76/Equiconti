package com.cz.equiconti.vm

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cz.equiconti.data.Repo
import com.cz.equiconti.data.Txn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/**
 * VM per la schermata dei movimenti di un cavallo.
 * Assume che in SavedStateHandle ci sia "horseId".
 */
@HiltViewModel
class TxnVm @Inject constructor(
    private val repo: Repo,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val horseId: Long = checkNotNull(savedStateHandle["horseId"])

    /** Flusso di movimenti (sola lettura) */
    val txns: StateFlow<List<Txn>> =
        repo.getTxns(horseId)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
}
