package com.cz.equiconti.ui.owner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cz.equiconti.data.Owner
import com.cz.equiconti.data.Repo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OwnersViewModel @Inject constructor(
    private val repo: Repo
) : ViewModel() {

    private val _owners = MutableStateFlow<List<Owner>>(emptyList())
    val owners: StateFlow<List<Owner>> = _owners.asStateFlow()

    init {
        // carico la lista una tantum; se hai Flow in repo puoi osservarlo in continuo
        viewModelScope.launch {
            _owners.value = repo.listOwnersOnce()
        }
    }

    suspend fun upsertOwner(owner: Owner): Long {
        val id = repo.upsertOwner(owner)
        refresh()
        return id
    }

    suspend fun getOwnerById(id: Long): Owner? = repo.getOwnerById(id)

    suspend fun deleteOwner(owner: Owner) {
        repo.deleteOwner(owner)
        refresh()
    }

    private fun refresh() {
        viewModelScope.launch {
            _owners.value = repo.listOwnersOnce()
        }
    }
}
