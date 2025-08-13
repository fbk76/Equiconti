package com.cz.equiconti.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TxnDao {
    @Query("SELECT * FROM Txn WHERE ownerId = :ownerId ORDER BY dateMillis DESC, txnId DESC")
    fun observeTxns(ownerId: Long): Flow<List<Txn>>

    @Insert
    suspend fun insert(txn: Txn): Long

    @Update
    suspend fun update(txn: Txn)

    @Delete
    suspend fun delete(txn: Txn)

    @Query("""
        SELECT * FROM Txn
        WHERE ownerId = :ownerId AND dateMillis BETWEEN :from AND :to
        ORDER BY dateMillis ASC, txnId ASC
    """)
    fun listInRange(ownerId: Long, from: Long, to: Long): Flow<List<Txn>>

    @Query("""
        SELECT COALESCE(SUM(incomeCents - expenseCents), 0)
        FROM Txn WHERE ownerId = :ownerId
    """)
    suspend fun balanceForOwner(ownerId: Long): Long

    @Query("""
        SELECT COALESCE(SUM(incomeCents - expenseCents), 0)
        FROM Txn WHERE ownerId = :ownerId AND dateMillis < :before
    """)
    suspend fun balanceBefore(ownerId: Long, before: Long): Long
}
