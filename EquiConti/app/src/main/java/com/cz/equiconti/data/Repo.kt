package com.cz.equiconti.data

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repo @Inject constructor(
    private val db: EquiDb
) {
    // ------- Owners -------
    fun getOwners(): Flow<List<Owner>> = db.ownerDao().getAll()
    fun getOwner(ownerId: Long): Flow<Owner?> = db.ownerDao().getById(ownerId)

    // ------- Horses -------
    fun getHorsesByOwner(ownerId: Long): Flow<List<Horse>> =
        db.horseDao().getByOwner(ownerId)

    suspend fun upsertHorse(horse: Horse) = db.horseDao().upsert(horse)
    suspend fun deleteHorse(horse: Horse) = db.horseDao().delete(horse)

    // ------- Transactions (Txns) -------
    fun getTxnsForOwner(ownerId: Long): Flow<List<Txn>> =
        db.txnDao().getByOwner(ownerId)

    suspend fun upsertTxn(txn: Txn) = db.txnDao().upsert(txn)
    suspend fun deleteTxn(txn: Txn) = db.txnDao().delete(txn)
}
