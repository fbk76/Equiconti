package com.cz.equiconti.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cz.equiconti.data.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class OwnersVm @Inject constructor(private val repo: Repo) : ViewModel() {
    private val _owners = MutableStateFlow<List<Owner>>(emptyList())
    val owners = _owners.asStateFlow()

    init { refresh() }

    fun refresh() = viewModelScope.launch {
        _owners.value = repo.listOwners()
    }

    fun addOwner(o: Owner) = viewModelScope.launch {
        repo.upsertOwner(o)
        refresh()
    }
}

@HiltViewModel
class OwnerDetailVm @Inject constructor(private val repo: Repo) : ViewModel() {
    private val _owner = MutableStateFlow<OwnerWithHorses?>(null)
    val owner = _owner.asStateFlow()

    private val _horses = MutableStateFlow<List<Horse>>(emptyList())
    val horses = _horses.asStateFlow()

    private val _balance = MutableStateFlow<Long?>(0L) // ← init come Long
    val balance = _balance.asStateFlow()

    fun load(ownerId: Long) = viewModelScope.launch {
        _owner.value = repo.getOwnerWithHorses(ownerId)
        _horses.value = repo.listHorses(ownerId)
        _balance.value = repo.db.txnDao().balanceForOwner(ownerId) ?: 0L
    }

    fun addHorse(h: Horse) = viewModelScope.launch {
        repo.upsertHorse(h)
        _horses.value = repo.listHorses(h.ownerId)
        _balance.value = repo.db.txnDao().balanceForOwner(h.ownerId) ?: 0L
    }
}

@HiltViewModel
class TxnVm @Inject constructor(private val repo: Repo) : ViewModel() {
    private val _txns = MutableStateFlow<List<Txn>>(emptyList())
    val txns = _tx
