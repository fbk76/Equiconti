package com.cz.equiconti.data

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repo @Inject constructor(
    private val db: EquiDb
) {
    // --- Flussi di sola lettura per la UI ---
    fun getOwners(): Flow<List<Owner>> = db.ownerDao().getOwners()

    /** Ritorna il singolo proprietario (può essere null se non esiste) */
    fun getOwner(ownerId: Long): Flow<Owner?> = db.ownerDao().getOwner(ownerId)

    /** Ritorna tutti i cavalli del proprietario */
    fun getHorses(ownerId: Long): Flow<List<Horse>> = db.horseDao().getHorses(ownerId)

    /** Movimenti per cavallo */
    fun getTxns(horseId: Long): Flow<List<Txn>> = db.txnDao().getTxns(horseId)

    // Se/Quando vorrai aggiungere scritture, abilita e implementa questi (e i DAO relativi)
    // suspend fun upsert(owner: Owner) = db.ownerDao().upsert(owner)
    // suspend fun upsert(horse: Horse) = db.horseDao().upsert(horse)
    // suspend fun insert(txn: Txn) = db.txnDao().insert(txn)
    // suspend fun delete(owner: Owner) = db.ownerDao().delete(owner)
    // suspend fun delete(horse: Horse) = db.horseDao().delete(horse)
    // suspend fun delete(txn: Txn) = db.txnDao().delete(txn)
}
