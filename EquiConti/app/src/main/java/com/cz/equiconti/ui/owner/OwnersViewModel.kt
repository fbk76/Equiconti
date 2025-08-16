package com.cz.equiconti.ui.owner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cz.equiconti.data.Horse
import com.cz.equiconti.data.Owner
import com.cz.equiconti.data.Repo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OwnersViewModel @Inject constructor(
    private val repo: Repo
) : ViewModel() {

    /** elenco proprietari (ordinati alfabeticamente per nome) */
    val owners: StateFlow<List<Owner>> =
        repo.getOwners()
            .map { it.sortedBy { o -> o.name.lowercase() } }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    /** proprietario singolo come Flow (per dettaglio) */
    fun ownerFlow(id: Long): Flow<Owner?> = repo.owner(id)

    /** cavalli per proprietario */
    fun horses(ownerId: Long): Flow<List<Horse>> = repo.getHorses(ownerId)

    /** crea/aggiorna un proprietario */
    fun saveOwner(name: String, phone: String?) = viewModelScope.launch {
        repo.upsert(Owner(name = name, phone = phone))
    }

    /** crea/aggiorna un cavallo */
    fun saveHorse(ownerId: Long, name: String, breed: String?) = viewModelScope.launch {
        repo.upsert(Horse(ownerId = ownerId, name = name, breed = breed))
    }
}
