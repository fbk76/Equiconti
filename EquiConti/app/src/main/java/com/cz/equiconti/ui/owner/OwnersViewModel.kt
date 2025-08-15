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

    // Lista proprietari (cognome+nome) osservabile
    val owners: StateFlow<List<Owner>> =
        repo.observeOwners()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    // Owner singolo on-demand (flow one-shot)
    fun ownerFlow(id: Long): Flow<Owner?> = flow { emit(repo.getOwnerById(id)) }

    // Flussi cavalli e movimenti per proprietario
    fun horses(ownerId: Long): Flow<List<Horse>> = repo.observeHorses(ownerId)
    fun txns(ownerId: Long): Flow<List<Txn>>   = repo.observeTxns(ownerId)

    // CRUD
    fun upsertOwner(owner: Owner) = viewModelScope.launch { repo.upsertOwner(owner) }
    fun deleteOwner(owner: Owner) = viewModelScope.launch { repo.deleteOwner(owner) }

    fun upsertHorse(horse: Horse) = viewModelScope.launch { repo.upsertHorse(horse) }
    fun deleteHorse(horse: Horse) = viewModelScope.launch { repo.deleteHorse(horse) }

    fun upsertTxn(txn: Txn) = viewModelScope.launch { repo.upsertTxn(txn) }
    fun deleteTxn(txn: Txn) = viewModelScope.launch { repo.deleteTxn(txn) }
}
