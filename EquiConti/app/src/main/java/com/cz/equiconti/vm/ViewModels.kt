// app/src/main/java/com/cz/equiconti/vm/ViewModels.kt
package com.cz.equiconti.vm

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// usa l'entity reale
import com.cz.equiconti.data.Owner

@HiltViewModel
class OwnersVm @Inject constructor() : ViewModel() {

    // stato osservabile dalla UI (lista clienti)
    private val _owners = MutableStateFlow<List<Owner>>(emptyList())
    val owners: StateFlow<List<Owner>> = _owners.asStateFlow()

    init {
        // placeholder: mostra qualcosa finché non colleghi Room
        _owners.value = listOf(
            Owner(id = 1L, name = "Fulvia", surname = "Bolzan", phone = "123456789")
        )
        // Quando colleghi Room/Hilt:
        // viewModelScope.launch {
        //     ownerDao.observeAll().collect { list -> _owners.value = list }
        // }
    }

    fun refresh() { /* no-op per ora */ }
}

@HiltViewModel
class OwnerDetailVm @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    val ownerId: Long = savedStateHandle["ownerId"] ?: 0L
}

@HiltViewModel
class ReportVm @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    val ownerId: Long = savedStateHandle["ownerId"] ?: 0L
}
