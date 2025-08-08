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
class OwnersVm @Inject constructor(
    private val repo: Repo
) : ViewModel() {

    private val _owners = MutableStateFlow<List<Owner>>(emptyList())
    val owners = _owners.asStateFlow()

    init {
        refresh()
    }

    fun refresh() = viewModelScope.launch {
        _owners.value = repo.listOwners()
    }

    fun addOwner(o: Owner) = viewModelScope.launch {
        repo.upsertOwner(o)
        refresh()
    }
}

@HiltViewModel
class OwnerDetailVm @Inject constructor(
    private val repo: Repo
) : ViewModel() {

    private val _owner = MutableStateFlow<OwnerWithHorses?>(null)
    val owner = _owner.asStateFlow()

    private val _horses = MutableStateFlow<List<Horse>>(emptyList())
    val horses = _horses.asStateFlow()

    private val _balance = MutableStateFlow<Long?>(0L)
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
class TxnVm @Inject constructor(
    private val repo: Repo
) : ViewModel() {

    private val _txns = MutableStateFlow<List<Txn>>(emptyList())
    val txns = _txns.asStateFlow()

    private var ownerId: Long = 0L

    fun load(ownerId: Long) = viewModelScope.launch {
        this@TxnVm.ownerId = ownerId
        _txns.value = repo.listTxns(ownerId)
    }

    fun addTxn(t: Txn) = viewModelScope.launch {
        repo.addTxn(t)
        _txns.value = repo.listTxns(ownerId)
    }
}

@HiltViewModel
class ReportVm @Inject constructor(
    private val repo: Repo
) : ViewModel() {

    private val _report = MutableStateFlow<Report?>(null)
    val report = _report.asStateFlow()

    fun load(ownerId: Long, from: String, to: String) = viewModelScope.launch {
        val rep = repo.report(ownerId, LocalDate.parse(from), LocalDate.parse(to))
        _report.value = rep
    }
}
