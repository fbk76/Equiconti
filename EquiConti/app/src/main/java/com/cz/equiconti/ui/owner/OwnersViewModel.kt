package com.cz.equiconti.ui.owner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cz.equiconti.data.Owner
import com.cz.equiconti.data.Repo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OwnersViewModel @Inject constructor(private val repo: Repo) : ViewModel() {
    val owners: StateFlow<List<Owner>> =
        repo.owners().map { it }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    fun addOwner(name: String) { viewModelScope.launch { repo.addOwner(name) } }
}
