package com.cz.equiconti.data

import android.content.Context
import kotlinx.coroutines.flow.first
import java.time.LocalDate
import java.time.ZoneId

class Repo(private val db: EquiDb) {

    companion object {
        fun from(context: Context) = Repo(EquiDb.get(context))
    }

    /* =============== OWNER =============== */

    // Se vuoi un elenco "one-shot" (non Flow) per uso interno
    suspend fun listOwnersOnce(): List<Owner> = db.ownerDao().observeAll().first()

    suspend fun getOwnerById(id: Long): Owner? = db.ownerDao().getById(id)

    suspend fun upsertOwner(owner: Owner): Long {
        return if (owner.id == 0L) {
            db.ownerDao().insert(owner)
        } else {
            db.ownerDao().update(owner); owner.id
        }
    }

    suspend fun deleteOwner(owner: Owner) = db.ownerDao().delete(owner)

    /* =============== HORSE =============== */

    suspend fun listHorsesOnce(ownerId: Long): List<Horse> =
        db.horseDao().observeByOwner(ownerId).first()

    suspend fun upsertHorse(h: Horse): Long {
        return if (h.id == 0L) db.horseDao().insert(h) else {
            db.horseDao().update(h); h.id
        }
    }

    suspend fun deleteHorse(h: Horse) = db.horseDao().delete(h)

    /* =============== TRANSAZIONI =============== */

    suspend fun addTxn(t: Txn): Long = db.txnDao().insert(t)
    suspend fun deleteTxn(t: Txn) = db.txnDao().delete(t)
    suspend fun updateTxn(t: Txn) = db.txnDao().update(t)

    suspend fun listTxnsOnce(ownerId: Long): List<Txn> =
        db.txnDao().listByOwner(ownerId).first()

    /* =============== REPORT =============== */

    suspend fun report(ownerId: Long, from: LocalDate, to: LocalDate): Report {
        val zone = ZoneId.systemDefault()
        val fromMillis = from.atStartOfDay(zone).toInstant().toEpochMilli()
        // includi tutto il giorno "to"
        val toMillis = to.plusDays(1).atStartOfDay(zone).toInstant().toEpochMilli() - 1

        val startBal = db.txnDao().balanceBefore(ownerId, fromMillis)
        val rows = db.txnDao().listInRange(ownerId, fromMillis, toMillis).first().map {
            ReportRow(
                dateMillis = it.dateMillis,
                operation = it.operation,
                incomeCents = it.incomeCents,
                expenseCents = it.expenseCents
            )
        }
        return Report(startBalanceCents = startBal, rows = rows)
    }

    /* =============== QUOTE MENSILI =============== */

    /**
     * Genera automaticamente le quote mensili il giorno 1 di ogni mese.
     * Somma le monthlyFeeCents di tutti i cavalli di ciascun owner e crea
     * una Txn di spesa (expenseCents) datata al 1° del mese.
     */
    suspend fun generateMonthlyFees(today: LocalDate) {
        if (today.dayOfMonth != 1) return

        val zone = ZoneId.systemDefault()
        val dateMillis = today.atStartOfDay(zone).toInstant().toEpochMilli()

        val owners = db.ownerDao().observeAll().first()
        for (o in owners) {
            val horses = db.horseDao().observeByOwner(o.id).first()
            val sumFees = horses.sumOf { it.monthlyFeeCents }
            if (sumFees > 0) {
                db.txnDao().insert(
                    Txn(
                        ownerId = o.id,
                        horseId = null,
                        dateMillis = dateMillis,
                        operation = "Quota mensile cavallo/i",
                        incomeCents = 0,
                        expenseCents = sumFees,
                        note = null
                    )
                )
            }
        }
    }
}

/* ======== Modelli per il report ======== */

data class Report(val startBalanceCents: Long, val rows: List<ReportRow>)

data class ReportRow(
    val dateMillis: Long,
    val operation: String,
    val incomeCents: Long,
    val expenseCents: Long
)
