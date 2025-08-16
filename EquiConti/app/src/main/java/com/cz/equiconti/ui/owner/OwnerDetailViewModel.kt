package com.cz.equiconti.ui.owner

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.cz.equiconti.data.Horse
import com.cz.equiconti.data.Owner
import com.cz.equiconti.data.Repo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OwnerDetailViewModel @Inject constructor(
    private val repo: Repo,
    savedStateHandle: SavedStateHandle
) : androidx.lifecycle.ViewModel() { // <- FQCN per evitare errori in kapt

    private val ownerId: Long = checkNotNull(savedStateHandle["ownerId"])

    val owner: StateFlow<Owner?> =
        repo.owner(ownerId)   // Assicurati che in Repo esista fun owner(id): Flow<Owner?>
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    val horses: StateFlow<List<Horse>> =
        repo.horses(ownerId)  // Assicurati che in Repo esista fun horses(ownerId): Flow<List<Horse>>
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun addHorse(name: String) {
        viewModelScope.launch { repo.addHorse(ownerId, name) } // id. per Repo.addHorse(...)
    }
}
