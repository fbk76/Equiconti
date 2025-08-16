package com.cz.equiconti.data

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repo @Inject constructor(
    private val db: EquiDb
) {
    // --- Letture reattive ---
    fun getOwners(): Flow<List<Owner>> = db.ownerDao().getOwners()
    fun getOwner(ownerId: Long): Flow<Owner?> = db.ownerDao().getOwner(ownerId)
    fun getHorses(ownerId: Long): Flow<List<Horse>> = db.horseDao().getHorses(ownerId)
    fun getTxns(horseId: Long): Flow<List<Txn>> = db.txnDao().getTxns(horseId)

    // --- Scritture di base ---
    suspend fun upsert(owner: Owner) = db.ownerDao().upsert(owner)
    suspend fun upsert(horse: Horse) = db.horseDao().upsert(horse)
    suspend fun insert(txn: Txn) = db.txnDao().insert(txn)
    suspend fun delete(owner: Owner) = db.ownerDao().delete(owner)
    suspend fun delete(horse: Horse) = db.horseDao().delete(horse)
    suspend fun delete(txn: Txn) = db.txnDao().delete(txn)

    // Comodità usata dal ViewModel dei dettagli
    suspend fun addHorse(ownerId: Long, name: String) =
        db.horseDao().upsert(Horse(ownerId = ownerId, name = name))
}
