package com.cz.equiconti.data

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repo @Inject constructor(
    private val db: EquiDb
) {
    /* ========= LETTURE REATTIVE ========= */
    fun getOwners(): Flow<List<Owner>> = db.ownerDao().getOwners()
    fun getHorses(ownerId: Long): Flow<List<Horse>> = db.horseDao().getHorses(ownerId)
    fun getTxns(horseId: Long): Flow<List<Txn>> = db.txnDao().getTxns(horseId)

    fun owner(id: Long): Flow<Owner?> = db.ownerDao().observeById(id)

    /* ========= LETTURE ONE-SHOT ========= */
    suspend fun getOwnerById(id: Long): Owner? = db.ownerDao().getById(id)

    /* ========= SCRITTURE ========= */
    suspend fun upsert(owner: Owner): Long = db.ownerDao().upsert(owner)
    suspend fun delete(owner: Owner) = db.ownerDao().delete(owner)

    suspend fun upsert(horse: Horse): Long = db.horseDao().upsert(horse)
    suspend fun delete(horse: Horse) = db.horseDao().delete(horse)

    suspend fun insert(txn: Txn): Long = db.txnDao().insert(txn)
    suspend fun delete(txn: Txn) = db.txnDao().delete(txn)
}
