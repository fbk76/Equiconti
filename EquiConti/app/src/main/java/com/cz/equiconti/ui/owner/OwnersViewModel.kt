package com.cz.equiconti.ui.owner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cz.equiconti.data.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OwnersViewModel @Inject constructor(
    private val repo: Repo
) : ViewModel() {

    /* ======== FLOWS PER LA UI ======== */

    // elenco proprietari (Flow -> StateFlow per Compose)
    val owners: StateFlow<List<Owner>> =
        repo.observeOwners()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    // singolo owner (usa una one-shot emit; se vuoi live updates, crea un DAO che osserva per id)
    fun ownerFlow(id: Long): Flow<Owner?> = flow {
        emit(repo.getOwnerById(id))
    }

    // cavalli/txns per owner
    fun horses(ownerId: Long): Flow<List<Horse>> = repo.observeHorses(ownerId)
    fun txns(ownerId: Long): Flow<List<Txn>> = repo.observeTxns(ownerId)

    /* ======== OPERAZIONI ======== */

    fun upsertOwner(owner: Owner) = viewModelScope.launch {
        repo.upsertOwner(owner)
    }

    fun deleteOwner(owner: Owner) = viewModelScope.launch {
        repo.deleteOwner(owner)
    }

    fun upsertHorse(horse: Horse) = viewModelScope.launch {
        repo.upsertHorse(horse)
    }

    fun deleteHorse(horse: Horse) = viewModelScope.launch {
        repo.deleteHorse(horse)
    }

    fun upsertTxn(txn: Txn) = viewModelScope.launch {
        repo.upsertTxn(txn)
    }

    fun deleteTxn(txn: Txn) = viewModelScope.launch {
        repo.deleteTxn(txn)
    }
}
