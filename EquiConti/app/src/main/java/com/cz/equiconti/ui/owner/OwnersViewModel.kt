package com.cz.equiconti.ui.owner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cz.equiconti.data.Horse
import com.cz.equiconti.data.Owner
import com.cz.equiconti.data.Repo
import com.cz.equiconti.data.Txn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OwnersViewModel @Inject constructor(
    private val repo: Repo
) : ViewModel() {

    /** Elenco proprietari osservabile */
    val owners: StateFlow<List<Owner>> =
        repo.observeOwners()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    /** Flusso one-shot del singolo proprietario (null se non trovato) */
    fun ownerFlow(id: Long): Flow<Owner?> = flow {
        emit(repo.getOwnerById(id))
    }

    /** CRUD Owner */
    fun upsertOwner(owner: Owner) = viewModelScope.launch {
        repo.upsertOwner(owner)
    }

    fun deleteOwner(owner: Owner) = viewModelScope.launch {
        repo.deleteOwner(owner)
    }

    /** Cavalli per owner */
    fun horses(ownerId: Long): Flow<List<Horse>> = repo.observeHorses(ownerId)

    /** Movimenti per owner */
    fun txns(ownerId: Long): Flow<List<Txn>> = repo.observeTxns(ownerId)

    /** CRUD Horse */
    fun upsertHorse(horse: Horse) = viewModelScope.launch {
        repo.upsertHorse(horse)
    }

    fun deleteHorse(horse: Horse) = viewModelScope.launch {
        repo.deleteHorse(horse)
    }

    /** CRUD Txn */
    fun addTxn(txn: Txn) = viewModelScope.launch {
        repo.addTxn(txn)
    }

    fun deleteTxn(txn: Txn) = viewModelScope.launch {
        repo.deleteTxn(txn)
    }
}
