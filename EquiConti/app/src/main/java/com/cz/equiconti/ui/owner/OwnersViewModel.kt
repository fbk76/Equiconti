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

    val owners: StateFlow<List<Owner>> =
        repo.observeOwners()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun ownerFlow(id: Long): Flow<Owner?> = flow { emit(repo.getOwnerById(id)) }

    fun upsertOwner(owner: Owner) = viewModelScope.launch { repo.upsertOwner(owner) }
    fun deleteOwner(owner: Owner) = viewModelScope.launch { repo.deleteOwner(owner) }

    fun horses(ownerId: Long): Flow<List<Horse>> = repo.observeHorses(ownerId)
    fun txns(ownerId: Long): Flow<List<Txn>> = repo.observeTxns(ownerId)

    fun upsertHorse(horse: Horse) = viewModelScope.launch { repo.upsertHorse(horse) }
    fun deleteHorse(horse: Horse) = viewModelScope.launch { repo.deleteHorse(horse) }

    fun addTxn(txn: Txn) = viewModelScope.launch { repo.addTxn(txn) }
    fun updateTxn(txn: Txn) = viewModelScope.launch { repo.updateTxn(txn) }
    fun deleteTxn(txn: Txn) = viewModelScope.launch { repo.deleteTxn(txn) }
}
