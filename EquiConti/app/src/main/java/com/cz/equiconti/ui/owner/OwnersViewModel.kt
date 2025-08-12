package com.cz.equiconti.ui.owner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cz.equiconti.data.Owner
import com.cz.equiconti.data.Repo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OwnersViewModel @Inject constructor(
    private val repo: Repo
) : ViewModel() {

    private val _owners = MutableStateFlow<List<Owner>>(emptyList())
    val owners: StateFlow<List<Owner>> = _owners.asStateFlow()

    init {
        viewModelScope.launch {
            repo.observeOwners().collectLatest { list ->
                _owners.value = list
            }
        }
    }

    /** Ritorna l’owner corrente (snapshot) se presente */
    fun getOwnerById(id: Long): Owner? = _owners.value.firstOrNull { it.id == id }

    /** Inserisce/aggiorna un owner */
    fun saveOwner(owner: Owner) {
        viewModelScope.launch {
            repo.upsertOwner(owner)
        }
    }

    /** Elimina un owner */
    fun removeOwner(ownerId: Long) {
        val owner = getOwnerById(ownerId) ?: return
        viewModelScope.launch {
            repo.deleteOwner(owner)
        }
    }
}
