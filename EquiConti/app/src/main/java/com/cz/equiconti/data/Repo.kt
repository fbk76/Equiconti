package com.cz.equiconti.data

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repo @Inject constructor(
    private val db: EquiDb
) {
    // ===== Owners =====
    fun getOwners(): Flow<List<Owner>> = db.ownerDao().getOwners()

    // ===== Horses =====
    fun getHorses(ownerId: Long): Flow<List<Horse>> = db.horseDao().getHorses(ownerId)

    suspend fun upsertHorse(horse: Horse): Long = db.horseDao().upsert(horse)
    suspend fun deleteHorse(horse: Horse) = db.horseDao().delete(horse)

    // ===== Transactions (se servono più avanti) =====
    fun getTxns(horseId: Long): Flow<List<Txn>> = db.txnDao().getTxns(horseId)
    // aggiungeremo qui insert/delete Txn quando colleghiamo la schermata Movimenti
}
