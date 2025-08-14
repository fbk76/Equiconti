package com.cz.equiconti.ui.owner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cz.equiconti.data.Horse
import com.cz.equiconti.data.Owner
import com.cz.equiconti.data.Repo
import com.cz.equiconti.data.Txn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OwnersViewModel @Inject constructor(
    private val repo: Repo
) : ViewModel() {

    /** Lista proprietari come StateFlow (per Compose). */
    val owners: StateFlow<List<Owner>> =
        repo.observeOwners()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    /** Singolo proprietario come Flow (riutilizzabile in Compose con collectAsState). */
    fun owner(id: Long): Flow<Owner?> = repo.observeOwnerById(id)

    /** Cavalli del proprietario. */
    fun horses(ownerId: Long): Flow<List<Horse>> = repo.observeHorses(ownerId)

    /** Movimenti del proprietario. */
    fun txns(ownerId: Long): Flow<List<Txn>> = repo.observeTxns(ownerId)

    /* =====================  Helper per la UI  ===================== */

    /** Crea/aggiorna un proprietario partendo da cognome/nome (senza telefono). */
    fun upsertOwner(lastName: String, firstName: String) = viewModelScope.launch {
        repo.upsertOwner(
            Owner(
                id = 0L,                // autoGenerate
                firstName = firstName.trim(),
                lastName = lastName.trim(),
                phone = null            // non usato
            )
        )
    }

    /** Crea/aggiorna un cavallo per il proprietario, con importo mensile in centesimi. */
    fun upsertHorse(ownerId: Long, name: String, amountCents: Long) = viewModelScope.launch {
        repo.upsertHorse(
            Horse(
                id = 0L,                 // autoGenerate
                ownerId = ownerId,
                name = name.trim(),
                monthlyFeeCents = amountCents,
                notes = null
            )
        )
    }

    /** Inserisce un movimento (entrate/uscite in centesimi, data in ms). */
    fun insertTxn(
        ownerId: Long,
        dateMillis: Long,
        operation: String,
        incomeCents: Long,
        expenseCents: Long
    ) = viewModelScope.launch {
        repo.upsertTxn(
            Txn(
                txnId = 0L,              // autoGenerate
                ownerId = ownerId,
                horseId = null,          // opzionale: collega a un cavallo se vuoi
                dateMillis = dateMillis,
                operation = operation.trim(),
                incomeCents = incomeCents,
                expenseCents = expenseCents,
                note = null
            )
        )
    }

    /* =====================  CRUD diretti (opzionali)  ===================== */

    fun upsertOwner(owner: Owner) = viewModelScope.launch { repo.upsertOwner(owner) }
    fun deleteOwner(owner: Owner) = viewModelScope.launch { repo.deleteOwner(owner) }

    fun upsertHorse(horse: Horse) = viewModelScope.launch { repo.upsertHorse(horse) }
    fun deleteHorse(horse: Horse) = viewModelScope.launch { repo.deleteHorse(horse) }

    fun upsertTxn(txn: Txn) = viewModelScope.launch { repo.upsertTxn(txn) }
    fun deleteTxn(txn: Txn) = viewModelScope.launch { repo.deleteTxn(txn) }
}
