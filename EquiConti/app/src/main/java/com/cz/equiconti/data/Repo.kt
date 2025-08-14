package com.cz.equiconti.data

import android.content.Context

class Repo(private val db: EquiDb) {

    companion object {
        fun from(context: Context) = Repo(EquiDb.get(context))
    }

    /* ======== OWNERS ======== */
    fun observeOwners() = db.ownerDao().observeAll()
    suspend fun getOwnerById(id: Long) = db.ownerDao().getById(id)

    suspend fun upsertOwner(owner: Owner): Long {
        return if (owner.id == 0L) {
            db.ownerDao().insert(owner)
        } else {
            db.ownerDao().update(owner); owner.id
        }
    }
    suspend fun deleteOwner(owner: Owner) = db.ownerDao().delete(owner)

    /* ======== HORSES ======== */
    fun observeHorses(ownerId: Long) = db.horseDao().observeByOwner(ownerId)

    suspend fun upsertHorse(horse: Horse): Long {
        return if (horse.id == 0L) {
            db.horseDao().insert(horse)
        } else {
            db.horseDao().update(horse); horse.id
        }
    }
    suspend fun deleteHorse(horse: Horse) = db.horseDao().delete(horse)

    /* ======== TXNS ======== */
    fun observeTxns(ownerId: Long) = db.txnDao().observeByOwner(ownerId)

    suspend fun upsertTxn(txn: Txn): Long {
        return if (txn.txnId == 0L) {
            db.txnDao().insert(txn)
        } else {
            db.txnDao().update(txn); txn.txnId
        }
    }
    suspend fun deleteTxn(txn: Txn) = db.txnDao().delete(txn)
}
