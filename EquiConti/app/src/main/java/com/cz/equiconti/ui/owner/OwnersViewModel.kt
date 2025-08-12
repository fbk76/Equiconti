package com.cz.equiconti.ui.owner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cz.equiconti.data.Owner
import com.cz.equiconti.data.Repo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OwnersViewModel @Inject constructor(
    private val repo: Repo
) : ViewModel() {

    // Lista proprietari osservabile dalla UI
    val owners: StateFlow<List<Owner>> =
        repo.observeOwners()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    // Dettaglio proprietario corrente (per schermata dettaglio/edit)
    private val _currentOwner = MutableStateFlow<Owner?>(null)
    val currentOwner: StateFlow<Owner?> get() = _currentOwner

    fun loadOwner(id: Long) {
        viewModelScope.launch {
            _currentOwner.value = repo.getOwnerById(id)
        }
    }

    fun upsert(owner: Owner, onDone: (Long) -> Unit = {}) {
        viewModelScope.launch {
            val id = repo.upsertOwner(owner)
            onDone(id)
        }
    }

    fun delete(owner: Owner, onDone: () -> Unit = {}) {
        viewModelScope.launch {
            repo.deleteOwner(owner)
            onDone()
        }
    }
}
