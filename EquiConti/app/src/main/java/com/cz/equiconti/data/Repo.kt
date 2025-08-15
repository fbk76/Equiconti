package com.cz.equiconti.data

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repo @Inject constructor(private val db: EquiDb) {

    // Owners
    fun owners(): Flow<List<Owner>> = db.ownerDao().getOwners()
    suspend fun addOwner(name: String): Long = db.ownerDao().insert(Owner(name = name))

    // Owner detail
    fun owner(ownerId: Long): Flow<Owner?> = db.ownerDao().getOwner(ownerId)
    fun horses(ownerId: Long): Flow<List<Horse>> = db.horseDao().getOwnerHorses(ownerId)
    suspend fun addHorse(ownerId: Long, name: String): Long =
        db.horseDao().insert(Horse(ownerId = ownerId, name = name))

    fun txns(ownerId: Long): Flow<List<Txn>> = db.txnDao().getOwnerTxns(ownerId)
    suspend fun addTxn(ownerId: Long, amount: Double, note: String?) =
        db.txnDao().insert(Txn(ownerId = ownerId, amount = amount, note = note))
}
