package com.cz.equiconti.ui.owner
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cz.equiconti.data.Horse
import com.cz.equiconti.data.Owner
import com.cz.equiconti.data.Repo
import com.cz.equiconti.data.Txn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OwnerDetailViewModel @Inject constructor(private val repo: Repo, savedStateHandle: SavedStateHandle) : ViewModel() {
    private val ownerId: Long = checkNotNull(savedStateHandle["ownerId"])
    val owner: StateFlow<Owner?> = repo.owner(ownerId).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)
    val horses: StateFlow<List<Horse>> = repo.horses(ownerId).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    val txns: StateFlow<List<Txn>> = repo.txns(ownerId).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    fun addHorse(name: String) { viewModelScope.launch { repo.addHorse(ownerId, name) } }
    fun addTxn(amount: Double, note: String?) { viewModelScope.launch { repo.addTxn(ownerId, amount, note) } }
}
