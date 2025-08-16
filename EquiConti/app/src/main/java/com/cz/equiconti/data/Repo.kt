package com.cz.equiconti.data

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repo @Inject constructor(
    private val db: EquiDb
) {
    // Stream reattivi per la UI
    fun getOwners(): Flow<List<Owner>> = db.ownerDao().getOwners()
    fun getHorses(ownerId: Long): Flow<List<Horse>> = db.horseDao().getHorses(ownerId)
    fun getTxns(horseId: Long): Flow<List<Txn>> = db.txnDao().getTxns(horseId)

    // --- Operazioni di scrittura ---
    // Al momento i DAO non hanno metodi @Upsert/@Insert/@Delete,
    // quindi li commentiamo per evitare errori di compilazione.
    // Quando aggiungi i metodi nei DAO, puoi riabilitarli.

    // suspend fun upsert(owner: Owner) = db.ownerDao().upsert(owner)
    // suspend fun upsert(horse: Horse) = db.horseDao().upsert(horse)
    // suspend fun insert(txn: Txn) = db.txnDao().insert(txn)
    // suspend fun delete(owner: Owner) = db.ownerDao().delete(owner)
    // suspend fun delete(horse: Horse) = db.horseDao().delete(horse)
    // suspend fun delete(txn: Txn) = db.txnDao().delete(txn)
}
