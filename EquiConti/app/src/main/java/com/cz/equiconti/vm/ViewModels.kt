package com.cz.equiconti.vm

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// Entities reali
import com.cz.equiconti.data.Owner
import com.cz.equiconti.data.Txn

/* ===================== OWNERS ===================== */

@HiltViewModel
class OwnersVm @Inject constructor() : ViewModel() {

    // Flusso osservabile dalla UI
    private val _owners = MutableStateFlow<List<Owner>>(emptyList())
    val owners: StateFlow<List<Owner>> = _owners.asStateFlow()

    init {
        // Placeholder finché non colleghiamo Room/Repo:
        _owners.value = listOf(
            Owner(id =
