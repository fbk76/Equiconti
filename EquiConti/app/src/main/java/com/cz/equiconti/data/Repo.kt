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

    fun observeOwners(): Flow<List<Owner>> = db.ownerDao().observeAll()

    suspend fun listOwnersOnce(): List<Owner> = db.ownerDao().observeAll().first()

    suspend fun getOwnerById(id: Long): Owner? = db.ownerDao().getById(id)

    /** Ritorna SEMPRE un Long esplicito per evitare l’errore “expected Long, actual Any”. */
    suspend fun upsertOwner(owner: Owner): Long {
        return if (owner.id == 0L) {
            db.ownerDao().insert(owner)              // Long
        } else {
            db.ownerDao().update(owner)              // Unit
            owner.id                                  // Long (ultimo valore del ramo)
        }
    }

    suspend fun deleteOwner(owner: Owner) {
        db.ownerDao().delete(owner)
    }

    /* =============== HORSE (se usi i cavalli) =============== */

    fun observeHorses(ownerId: Long): Flow<List<Horse>> =
        db.horseDao().observeByOwner(ownerId)

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

    suspend fun deleteHorse(horse: Horse) {
        db.horseDao().delete(horse)
    }

    /* =============== TXN (se usi i movimenti) =============== */

    fun observeTxns(ownerId: Long): Flow<List<Txn>> =
        db.txnDao().listByOwner(ownerId)

    suspend fun listTxnsOnce(ownerId: Long): List<Txn> =
        db.txnDao().listByOwner(ownerId).first()

    suspend fun addTxn(txn: Txn): Long = db.txnDao().insert(txn)

    // (facoltativo) solo se nel tuo TxnDao esiste @Delete
    // suspend fun deleteTxn(txn: Txn) = db.txnDao().delete(txn)

    /* =============== UTIL =============== */

    fun dayBounds(date: LocalDate): Pair<Long, Long> {
        val start = date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        val end = date.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli() - 1
        return start to end
    }
}
