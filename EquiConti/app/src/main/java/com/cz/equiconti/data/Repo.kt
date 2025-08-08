package com.cz.equiconti.data

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate

class Repo(private val db: EquiDb) {

    companion object {
        fun from(context: Context) = Repo(EquiDb.get(context))
    }

    suspend fun listOwners() = db.ownerDao().listOwners()
    suspend fun getOwnerWithHorses(id: Long) = db.ownerDao().loadWithHorses(id)
    suspend fun upsertOwner(owner: Owner): Long {
        return if (owner.ownerId == 0L) db.ownerDao().insert(owner) else {
            db.ownerDao().update(owner); owner.ownerId
        }
    }
    suspend fun deleteOwner(owner: Owner) = db.ownerDao().delete(owner)

    suspend fun listHorses(ownerId: Long) = db.horseDao().listByOwner(ownerId)
    suspend fun upsertHorse(h: Horse): Long {
        return if (h.horseId == 0L) db.horseDao().insert(h) else {
            db.horseDao().update(h); h.horseId
        }
    }
    suspend fun deleteHorse(h: Horse) = db.horseDao().delete(h)

    suspend fun addTxn(t: Txn) = db.txnDao().insert(t)
    suspend fun updateTxn(t: Txn) = db.txnDao().update(t)
    suspend fun deleteTxn(t: Txn) = db.txnDao().delete(t)
    suspend fun listTxns(ownerId: Long) = db.txnDao().listByOwner(ownerId)

    suspend fun report(ownerId: Long, from: LocalDate, to: LocalDate): Report {
        val startBal = db.txnDao().balanceBefore(ownerId, from.toString()) ?: 0L
        val rows = db.txnDao().listInRange(ownerId, from.toString(), to.toString()).map {
            ReportRow(
                date = it.date,
                operation = it.operation,
                incomeCents = it.incomeCents,
                expenseCents = it.expenseCents
            )
        }
        return Report(startBalanceCents = startBal, rows = rows)
    }

    // Generate monthly fees on 1st day: one sum per owner (sum of horse monthly fees)
    suspend fun generateMonthlyFees(date: LocalDate) {
        if (date.dayOfMonth != 1) return
        val owners = db.ownerDao().listOwners()
        for (o in owners) {
            val horses = db.horseDao().listByOwner(o.ownerId)
            val sumFees = horses.sumOf { it.monthlyFeeCents }
            if (sumFees > 0) {
                db.txnDao().insert(
                    Txn(
                        ownerId = o.ownerId,
                        date = date.toString(),
                        operation = "Quota mensile cavallo/i",
                        incomeCents = 0,
                        expenseCents = sumFees
                    )
                )
            }
        }
    }
}

data class Report(val startBalanceCents: Long, val rows: List<ReportRow>)
data class ReportRow(
    val date: String,
    val operation: String,
    val incomeCents: Long,
    val expenseCents: Long
)
