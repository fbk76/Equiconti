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

    /* =============== OWNER =============== */

    /** Flow continuo di tutti i proprietari */
    fun observeOwners(): Flow<List<Owner>> = db.ownerDao().observeAll()

    /** Snapshot una-tantum dei proprietari */
    suspend fun listOwnersOnce(): List<Owner> = db.ownerDao().observeAll().first()

    suspend fun getOwnerById(id: Long): Owner? = db.ownerDao().getById(id)

    suspend fun upsertOwner(owner: Owner): Long {
        return if (owner.id == 0L) {
            db.ownerDao().insert(owner)
        } else {
            db.ownerDao().update(owner)
            owner.id
        }
    }

    suspend fun deleteOwner(owner: Owner) = db.ownerDao().delete(owner)

    /* =============== HORSE =============== */

    /** Flow dei cavalli di un proprietario */
    fun observeHorses(ownerId: Long): Flow<List<Horse>> =
        db.horseDao().observeByOwner(ownerId)

    /** Snapshot una-tantum dei cavalli di un proprietario */
    suspend fun listHorsesOnce(ownerId: Long): List<Horse> =
        db.horseDao().observeByOwner(ownerId).first()

    suspend fun upsertHorse(horse: Horse): Long {
        return if (horse.id == 0L) {
            db.horseDao().insert(horse)
        } else {
            db.horseDao().update(horse)
            horse.id
        }
    }

    suspend fun deleteHorse(horse: Horse) = db.horseDao().delete(horse)

    /* =============== TXN =============== */

    /** Flow dei movimenti di un proprietario */
    fun observeTxns(ownerId: Long): Flow<List<Txn>> =
        db.txnDao().observeByOwner(ownerId)

    /** Snapshot una-tantum dei movimenti di un proprietario */
    suspend fun listTxnsOnce(ownerId: Long): List<Txn> =
        db.txnDao().observeByOwner(ownerId).first()

    suspend fun addTxn(txn: Txn): Long = db.txnDao().insert(txn)

    suspend fun deleteTxn(txn: Txn) = db.txnDao().delete(txn)

    /* =============== UTIL =============== */

    /** Inizio/fine giorno in millis (se ti servono per report) */
    fun dayBounds(date: LocalDate): Pair<Long, Long> {
        val start = date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        val end = date.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli() - 1
        return start to end
    }
}
