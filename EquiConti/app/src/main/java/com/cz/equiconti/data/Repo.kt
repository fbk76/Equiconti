package com.cz.equiconti.data

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repo @Inject constructor(
    private val db: EquiDb
) {
    // stream reattivi
    fun getOwners(): Flow<List<Owner>> = db.ownerDao().getOwners()
    fun getHorses(ownerId: Long): Flow<List<Horse>> = db.horseDao().getHorses(ownerId)
    fun getTxns(horseId: Long): Flow<List<Txn>> = db.txnDao().getTxns(horseId)

    // scritture per Txn (coerenti con il tuo TxnDao)
    suspend fun insert(txn: Txn): Long = db.txnDao().insert(txn)
    suspend fun delete(txn: Txn) = db.txnDao().delete(txn)

    // (eventuali metodi per Owner/Horse li puoi aggiungere quando servono)
}
