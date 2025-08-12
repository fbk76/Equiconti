package com.cz.equiconti.ui.owner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cz.equiconti.data.Owner
import com.cz.equiconti.data.Repo
import com.cz.equiconti.data.Horse
import com.cz.equiconti.data.Txn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OwnersViewModel @Inject constructor(
    private val repo: Repo
) : ViewModel() {

    // elenco proprietari
    val owners: StateFlow<List<Owner>> =
        repo.observeOwners()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    // flusso del singolo owner
    fun ownerFlow(id: Long): Flow<Owner?> = flow {
        emit(repo.getOwnerById(id))
    }

    fun upsertOwner(owner: Owner) {
        viewModelScope.launch { repo.upsertOwner(owner) }
    }

    fun deleteOwner(owner: Owner) {
        viewModelScope.launch { repo.deleteOwner(owner) }
    }

    // cavalli per owner
    fun horses(ownerId: Long): Flow<List<Horse>> = repo.observeHorses(ownerId)

    // movimenti per owner
    fun txns(ownerId: Long): Flow<List<Txn>> = repo.observeTxns(ownerId)
}
