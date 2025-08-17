package com.cz.equiconti.ui.txn

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cz.equiconti.data.Repo
import com.cz.equiconti.data.Txn
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class TxnViewModel @Inject constructor(
    repo: Repo,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val ownerId: Long = checkNotNull(savedStateHandle.get<Long>("ownerId"))

    val txns: StateFlow<List<Txn>> =
        repo.getTxns(ownerId)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
}
