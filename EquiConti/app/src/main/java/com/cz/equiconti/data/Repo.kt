package com.cz.equiconti.data
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton
@Singleton
class Repo @Inject constructor(private val db: EquiDb) {
    fun owners(): Flow<List<Owner>> = db.ownerDao().getOwners()
    suspend fun addOwner(name: String): Long = db.ownerDao().insert(Owner(name = name))
    fun owner(ownerId: Long) = db.ownerDao().getOwner(ownerId)
    fun horses(ownerId: Long) = db.horseDao().getOwnerHorses(ownerId)
    suspend fun addHorse(ownerId: Long, name: String) = db.horseDao().insert(Horse(ownerId = ownerId, name = name))
    fun txns(ownerId: Long) = db.txnDao().getOwnerTxns(ownerId)
    suspend fun addTxn(ownerId: Long, amount: Double, note: String?) = db.txnDao().insert(Txn(ownerId = ownerId, amount = amount, note = note))
}
