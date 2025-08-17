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
import javax.inject.Inject

@HiltViewModel
class TxnViewModel @Inject constructor(
    private val repo: Repo,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val ownerId: Long = savedStateHandle["ownerId"] ?: 0L

    val txns: StateFlow<List<Txn>> =
        repo.getTxns(ownerId)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
}
