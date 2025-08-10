package com.cz.equiconti.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/* ============= OWNER ============= */

@Dao
interface OwnerDao {

    // Usato in Repo.report() e Repo.generateMonthlyFees()
    @Query("SELECT * FROM Owner ORDER BY lastName, firstName")
    fun observeAll(): Flow<List<Owner>>

    @Query("SELECT * FROM Owner WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): Owner?

    @Insert
    suspend fun insert(owner: Owner): Long

    @Update
    suspend fun update(owner: Owner)

    @Delete
    suspend fun delete(owner: Owner)
}

/* ============= HORSE ============= */

@Dao
interface HorseDao {

    // Usato in Repo.generateMonthlyFees()
    @Query("SELECT * FROM Horse WHERE ownerId = :ownerId ORDER BY name")
    fun observeByOwner(ownerId: Long): Flow<List<Horse>>

    @Insert
    suspend fun insert(horse: Horse): Long

    @Update
    suspend fun update(horse: Horse)

    @Delete
    suspend fun delete(horse: Horse)
}

/* ============= TXN ============= */

@Dao
interface TxnDao {

    @Insert
    suspend fun insert(txn: Txn): Long

    @Update
    suspend fun update(txn: Txn)

    @Delete
    suspend fun delete(txn: Txn)

    // 🔴 Niente "id": ordiniamo per dateMillis, poi txnId
    @Query("""
        SELECT * FROM Txn
        WHERE ownerId = :ownerId
        ORDER BY dateMillis DESC, txnId DESC
    """)
    fun listByOwner(ownerId: Long): Flow<List<Txn>>

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

    @Query("""
        SELECT * FROM Txn
        WHERE ownerId = :ownerId
          AND dateMillis BETWEEN :from AND :to
        ORDER BY dateMillis DESC, txnId DESC
    """)
    fun listInRange(ownerId: Long, from: Long, to: Long): Flow<List<Txn>>
}
