package com.cz.equiconti.vm

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.cz.equiconti.data.Owner

/* ====== OWNERS ====== */
@HiltViewModel
class OwnersVm @Inject constructor() : ViewModel() {

    private val _owners = MutableStateFlow<List<Owner>>(emptyList())
    val owners: StateFlow<List<Owner>> = _owners.asStateFlow()

    init {
        // Usa i campi correnti del tuo Owner (name/surname se quello è il modello attivo)
        _owners.value = listOf(
            Owner(id = 1L, name = "Fulvia", surname = "Bolzan", phone = "123456789")
        )
    }

    fun refresh() { /* no-op per ora */ }
}

/* ====== OWNER DETAIL ====== */
@HiltViewModel
class OwnerDetailVm @Inject constructor() : ViewModel()

/* ====== REPORT ====== */
@HiltViewModel
class ReportVm @Inject constructor() : ViewModel()
