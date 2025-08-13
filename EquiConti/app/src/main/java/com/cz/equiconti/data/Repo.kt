package com.cz.equiconti.data

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import java.time.LocalDate
import java.time.ZoneId

class Repo(private val db: EquiDb) {

    companion object {
        fun from(context: Context) = Repo(EquiDb.get(context))
    }

    /* Owners */
    fun observeOwners(): Flow<List<Owner>> = db.ownerDao().observeAll()
    suspend fun getOwnerById(id: Long): Owner? = db.ownerDao().getById(id)
    suspend fun upsertOwner(owner: Owner): Long =
        if (owner.id == 0L) db.ownerDao().insert(owner) else { db.ownerDao().update(owner); owner.id }
    suspend fun deleteOwner(owner: Owner) = db.ownerDao().delete(owner)

    /* Horses */
    fun observeHorses(ownerId: Long): Flow<List<Horse>> = db.horseDao().observeHorses(ownerId)
    suspend fun upsertHorse(horse: Horse): Long =
        if (horse.id == 0L) db.horseDao().insert(horse) else { db.horseDao().update(horse); horse.id }
    suspend fun deleteHorse(horse: Horse) = db.horseDao().delete(horse)

    /* Txns */
    fun observeTxns(ownerId: Long): Flow<List<Txn>> = db.txnDao().observeTxns(ownerId)
    suspend fun addTxn(txn: Txn): Long = db.txnDao().insert(txn)
    suspend fun updateTxn(txn: Txn) = db.txnDao().update(txn)
    suspend fun deleteTxn(txn: Txn) = db.txnDao().delete(txn)

    /* Util */
    fun dayBounds(date: LocalDate): Pair<Long, Long> {
        val zone = ZoneId.systemDefault()
        val start = date.atStartOfDay(zone).toInstant().toEpochMilli()
        val end = date.plusDays(1).atStartOfDay(zone).toInstant().toEpochMilli() - 1
        return start to end
    }

    suspend fun listOwnersOnce(): List<Owner> = observeOwners().first()
}
