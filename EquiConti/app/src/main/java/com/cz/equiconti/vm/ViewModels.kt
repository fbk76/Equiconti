package com.cz.equiconti.vm

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// Se hai già la tua entity Owner, importa quella:
// import com.cz.equiconti.data.Owner

// --- fallback semplice per compilare subito ---
// Eliminalo se usi la tua entity com.cz.equiconti.data.Owner
data class Owner(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val phone: String? = null
)
// ------------------------------------------------

@HiltViewModel
class OwnersVm @Inject constructor(
    // Se/Quando vorrai usare un DAO o un Repository, iniettalo qui
    // private val ownerDao: OwnerDao
) : ViewModel() {

    // Stato pubblico osservabile dalla UI
    private val _owners = MutableStateFlow<List<Owner>>(emptyList())
    val owners: StateFlow<List<Owner>> = _owners.asStateFlow()

    init {
        // Per ora carichiamo un mock così l'app mostra qualcosa
        viewModelScope.launch {
            _owners.value = listOf(
                Owner(id = 1L, firstName = "Fulvia", lastName = "Bolzan", phone = "123456789")
            )
        }

        // Esempio con DAO (da usare quando avrai il DAO):
        // viewModelScope.launch {
        //     ownerDao.observeAll().collect { list -> _owners.value = list }
        // }
    }

    fun refresh() {
        // hook per la UI; no-op per ora
    }
}

@HiltViewModel
class OwnerDetailVm @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    val ownerId: Long = savedStateHandle["ownerId"] ?: 0L
    // Aggiungi qui stato/logica del dettaglio quando serve
}

@HiltViewModel
class ReportVm @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    val ownerId: Long = savedStateHandle["ownerId"] ?: 0L
    // Aggiungi qui stato/logica del report quando serve
}
