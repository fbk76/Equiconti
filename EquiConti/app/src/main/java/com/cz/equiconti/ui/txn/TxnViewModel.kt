package com.cz.equiconti.ui.txn

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cz.equiconti.data.Repo
import com.cz.equiconti.data.Txn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TxnViewModel @Inject constructor(
    private val repo: Repo
) : ViewModel() {

    fun txns(horseId: Long): Flow<List<Txn>> = repo.getTxns(horseId)

    fun addTxn(horseId: Long, amountCents: Long, note: String?) {
        viewModelScope.launch {
            repo.insert(
                Txn(
                    horseId = horseId,
                    amountCents = amountCents,
                    note = note
                )
            )
        }
    }
}
