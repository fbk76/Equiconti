package com.cz.equiconti.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TxnDao {

    @Insert
    suspend fun insert(txn: Txn): Long

    // Lista movimenti per owner (serve a TxnScreen)
    @Query("SELECT * FROM Txn WHERE ownerId = :ownerId ORDER BY dateMillis DESC, txnId DESC")
    fun listByOwner(ownerId: Long): Flow<List<Txn>>

    // --- Opzionali ma utili per Report/Repo (già coerenti con i modelli) ---

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
