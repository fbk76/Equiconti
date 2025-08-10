package com.cz.equiconti.vm

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

import com.cz.equiconti.data.Owner
import com.cz.equiconti.data.Txn

/* ===================== OWNERS ===================== */
@HiltViewModel
class OwnersVm @Inject constructor() : ViewModel() {

    private val _owners = MutableStateFlow<List<Owner>>(emptyList())
    val owners: StateFlow<List<Owner>> = _owners.asStateFlow()

    init {
        _owners.value = listOf(
            Owner(id = 1L, firstName = "Fulvia", lastName = "Bolzan", phone = "123456789")
        )
    }

    fun refresh() {
        // per ora non fa nulla, placeholder
    }
}

/* ===================== OWNER DETAIL ===================== */
@HiltViewModel
class OwnerDetailVm @Inject constructor() : ViewModel()

/* ===================== REPORT ===================== */
@HiltViewModel
class ReportVm @Inject constructor() : ViewModel()

/* ===================== TRANSAZIONI ===================== */
@HiltViewModel
class TxnVm @Inject constructor() : ViewModel() {

    private val _txns = MutableStateFlow<List<Txn>>(emptyList())
    val txns: StateFlow<List<Txn>> = _txns.asStateFlow()

    fun load(ownerId: Long) {
        _txns.value = emptyList() // nessun dato per ora
    }

    fun addTxn(t: Txn) {
        _txns.value = _txns.value + t
    }
}
