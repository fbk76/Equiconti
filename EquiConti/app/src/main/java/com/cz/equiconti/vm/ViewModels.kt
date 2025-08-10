package com.cz.equiconti.vm

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

    // stato osservabile dalla UI
    private val _owners = MutableStateFlow<List<Owner>>(emptyList())
    val owners: StateFlow<List<Owner>> = _owners.asStateFlow()

    init {
        // mock minimale coerente con Owner(id, firstName, lastName, phone)
        _owners.value = listOf(
            Owner(id = 1L, firstName = "Fulvia", lastName = "Bolzan", phone = "123456789")
        )
    }

    fun refresh() { /* no-op per ora */ }
}

@HiltViewModel
class OwnerDetailVm @Inject constructor() : ViewModel() {
    // aggiungi stato/logica del dettaglio quando serve
}

@HiltViewModel
class ReportVm @Inject constructor() : ViewModel() {
    // aggiungi stato/logica del report quando serve
}
