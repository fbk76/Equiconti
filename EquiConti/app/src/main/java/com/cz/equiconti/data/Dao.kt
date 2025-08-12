package com.cz.equiconti.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/* ======================= OWNER ======================= */

@Dao
interface OwnerDao {

    // Usato da Repo.observeOwners()
    @Query("SELECT * FROM Owner ORDER BY lastName, firstName")
    fun observeAll(): Flow<List<Owner>>

    // Usato da Repo.getOwnerById(...)
    @Query("SELECT * FROM Owner WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): Owner?

    @Insert
    suspend fun insert(owner: Owner): Long

    @Update
    suspend fun update(owner: Owner)

    @Delete
    suspend fun delete(owner: Owner)
}

/* ======================= HORSE ======================= */

@Dao
interface HorseDao {

    // Usato da Repo.observeHorses(ownerId)
    @Query("SELECT * FROM Horse WHERE ownerId = :ownerId ORDER BY name")
    fun observeByOwner(ownerId: Long): Flow<List<Horse>>

    @Insert
    suspend fun insert(horse: Horse): Long

    @Update
    suspend fun update(horse: Horse)

    @Delete
    suspend fun delete(horse: Horse)

    @Query("SELECT * FROM Horse WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): Horse?
}

/* ======================== TXN ======================== */

@Dao
interface TxnDao {

    // Usato da Repo.observeTxns(ownerId)
    @Query("SELECT * FROM Txn WHERE ownerId = :ownerId ORDER BY dateMillis DESC, txnId DESC")
    fun listByOwner(ownerId: Long): Flow<List<Txn>>

    @Insert
    suspend fun insert(txn: Txn): Long

    @Delete
    suspend fun delete(txn: Txn)

    /* ---- Opzionali utili (se fai report/filtri) ---- */

    @Query("""
        SELECT * FROM Txn
        WHERE ownerId = :ownerId
          AND dateMillis BETWEEN :from AND :to
        ORDER BY dateMillis ASC, txnId ASC
    """)
    fun listInRange(ownerId: Long, from: Long, to: Long): Flow<List<Txn>>

    @Query("""
        SELECT COALESCE(SUM(incomeCents - expenseCents), 0)
        FROM Txn
        WHERE ownerId = :ownerId
    """)
    suspend fun balanceForOwner(ownerId: Long): Long

    @Query("""
        SELECT COALESCE(SUM(incomeCents - expenseCents), 0)
        FROM Txn
        WHERE ownerId = :ownerId AND dateMillis < :before
    """)
    suspend fun balanceBefore(ownerId: Long, before: Long): Long
}
