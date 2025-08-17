package com.cz.equiconti.ui.owner

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cz.equiconti.data.Horse
import com.cz.equiconti.data.Owner
import com.cz.equiconti.data.Repo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class OwnerDetailViewModel @Inject constructor(
    repo: Repo,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val ownerId: Long = checkNotNull(savedStateHandle.get<Long>("ownerId"))

    val owner: StateFlow<Owner?> =
        repo.getOwner(ownerId)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    val horses: StateFlow<List<Horse>> =
        repo.getHorses(ownerId)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
}
