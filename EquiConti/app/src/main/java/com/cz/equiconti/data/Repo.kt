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

    /* ===================== OWNER ===================== */

    /** Flow continuo di tutti i proprietari (usato dalla OwnersViewModel). */
    fun observeOwners(): Flow<List<Owner>> = db.ownerDao().observeAll()

    /** Snapshot one-shot del singolo proprietario, o null se non esiste. */
    suspend fun getOwnerById(id: Long): Owner? = db.ownerDao().getById(id)

    /** Inserisce o aggiorna un proprietario. Ritorna l'id. */
    suspend fun upsertOwner(owner: Owner): Long {
        return if (owner.id == 0L) {
            db.ownerDao().insert(owner)
        } else {
            db.ownerDao().update(owner)
            owner.id
        }
    }

    suspend fun deleteOwner(owner: Owner) = db.ownerDao().delete(owner)

    /* ===================== HORSE ===================== */

    /** Flow dei cavalli di un proprietario (lista reattiva). */
    fun observeHorses(ownerId: Long): Flow<List<Horse>> =
        db.horseDao().observeByOwner(ownerId)

    /** Inserisce o aggiorna un cavallo. Ritorna l'id. */
    suspend fun upsertHorse(horse: Horse): Long {
        return if (horse.id == 0L) {
            db.horseDao().insert(horse)
        } else {
            db.horseDao().update(horse)
            horse.id
        }
    }

    suspend fun deleteHorse(horse: Horse) = db.horseDao().delete(horse)

    /* ===================== TXN ===================== */

    /** Flow dei movimenti per un proprietario (ordinati per data). */
    fun observeTxns(ownerId: Long): Flow<List<Txn>> =
        db.txnDao().listByOwner(ownerId)

    /** Aggiunge un movimento. Ritorna l'id. */
    suspend fun addTxn(txn: Txn): Long = db.txnDao().insert(txn)

    /** Elimina un movimento. */
    suspend fun deleteTxn(txn: Txn) = db.txnDao().delete(txn)

    /* ===================== UTIL (opzionale) ===================== */

    /** Restituisce [start,end] in millis per il giorno indicato (utile per report/filtri). */
    fun dayBounds(date: LocalDate): Pair<Long, Long> {
        val zone = ZoneId.systemDefault()
        val start = date.atStartOfDay(zone).toInstant().toEpochMilli()
        val end = date.plusDays(1).atStartOfDay(zone).toInstant().toEpochMilli() - 1
        return start to end
    }

    /** Esempio snapshot una-tantum (se mai dovesse servirti). */
    suspend fun listOwnersOnce(): List<Owner> = observeOwners().first()
}
