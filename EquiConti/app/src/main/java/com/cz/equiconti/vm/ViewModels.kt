package com.cz.equiconti.vm

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// Usa l'entity reale dal package data
import com.cz.equiconti.data.Owner

@HiltViewModel
class OwnersVm @Inject constructor(
    // Quando vorrai usare davvero Room/Hilt, inietterai qui OwnerDao/Repository
    // private val ownerDao: OwnerDao
) : ViewModel() {

    // Stato osservabile dalla UI (lista clienti)
    private val _owners = MutableStateFlow<List<Owner>>(emptyList())
    val owners: StateFlow<List<Owner>> = _owners.asStateFlow()

    init {
        // Placeholder: un record di esempio finché non colleghi il DAO
        _owners.value = listOf(
            Owner(id = 1L, firstName = "Fulvia", lastName = "Bolzan", phone = "123456789")
        )

        // Esempio quando userai il DAO:
        // viewModelScope.launch {
        //     ownerDao.getAllOwners().collect { list -> _owners.value = list }
        // }
    }

    fun refresh() {
        // hook per ricaricare dati (no-op per ora)
    }
}

@HiltViewModel
class OwnerDetailVm @Inject constructor() : ViewModel() {
    // Aggiungi stato/logica del dettaglio quando serve
}

@HiltViewModel
class ReportVm @Inject constructor() : ViewModel() {
    // Aggiungi stato/logica del report quando serve
}
