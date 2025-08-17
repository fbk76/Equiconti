package com.cz.equiconti.data

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository centrale che delega alle DAO.
 * - Owners: getAll, getById, upsert, delete
 * - Horses: getByOwner, getById, upsert, delete
 * - Txns:   getByOwner, upsert, delete
 */
@Singleton
class Repo @Inject constructor(
    private val ownerDao: OwnerDao,
    private val horseDao: HorseDao,
    private val txnDao: TxnDao
) {
    // -------- Owners --------
    fun getOwners(): Flow<List<Owner>> = ownerDao.getAll()
    fun getOwner(ownerId: Long): Flow<Owner?> = ownerDao.getById(ownerId)
    suspend fun upsertOwner(owner: Owner) = ownerDao.upsert(owner)
    suspend fun deleteOwner(owner: Owner) = ownerDao.delete(owner)

    // -------- Horses --------
    /** Tutti i cavalli di un proprietario */
    fun getHorses(ownerId: Long): Flow<List<Horse>> = horseDao.getByOwner(ownerId)
    fun getHorse(horseId: Long): Flow<Horse?> = horseDao.getById(horseId)
    suspend fun upsertHorse(horse: Horse) = horseDao.upsert(horse)
    suspend fun deleteHorse(horse: Horse) = horseDao.delete(horse)

    // -------- Transactions --------
    /** Tutti i movimenti dei cavalli di un proprietario */
    fun getTxns(ownerId: Long): Flow<List<Txn>> = txnDao.getByOwner(ownerId)
    suspend fun upsertTxn(txn: Txn) = txnDao.upsert(txn)
    suspend fun deleteTxn(txn: Txn) = txnDao.delete(txn)
}
