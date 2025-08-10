// app/src/main/java/com/cz/equiconti/data/Dao.kt
package com.cz.equiconti.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/* =======================
 * OWNER
 * ======================= */
@Dao
interface OwnerDao {

    /** Stream reattivo di tutti i clienti, ordinati per cognome/nome */
    @Query("SELECT * FROM Owner ORDER BY lastName, firstName")
    fun observeAll(): Flow<List<Owner>>

    /** Lettura one–shot di un cliente per id */
    @Query("SELECT * FROM Owner WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): Owner?

    @Insert
    suspend fun insert(owner: Owner): Long

    @Update
    suspend fun update(owner: Owner)

    @Delete
    suspend fun delete(owner: Owner)
}

/* =======================
 * HORSE
 * ======================= */
@Dao
interface HorseDao {

    /** Stream dei cavalli per owner */
    @Query("SELECT * FROM Horse WHERE ownerId = :ownerId ORDER BY name")
    fun observeByOwner(ownerId: Long): Flow<List<Horse>>

    @Insert
    suspend fun insert(horse: Horse): Long

    @Update
    suspend fun update(horse: Horse)

    @Delete
    suspend fun delete(horse: Horse)
}

/* =======================
 * TXN (movimenti)
 * ======================= */
@Dao
interface TxnDao {

    @Insert
    suspend fun insert(txn: Txn): Long

    @Update
    suspend fun update(txn: Txn)

    @Delete
    suspend fun delete(txn: Txn)

    /** Stream dei movimenti per owner, dal più recente */
    @Query("""
        SELECT * FROM Txn
        WHERE ownerId = :ownerId
        ORDER BY dateMillis DESC, id DESC
    """)
    fun listByOwner(ownerId: Long): Flow<List<Txn>>

    /** Stream dei movimenti in un intervallo [from, to] (millisecondi epoch) */
    @Query("""
        SELECT * FROM Txn
        WHERE ownerId = :ownerId
          AND dateMillis BETWEEN :from AND :to
        ORDER BY dateMillis DESC, id DESC
    """)
    fun listInRange(ownerId: Long, from: Long, to: Long): Flow<List<Txn>>

    /** Saldo complessivo (entrate - uscite) per owner */
    @Query("""
        SELECT COALESCE(SUM(incomeCents - expenseCents), 0)
        FROM Txn
        WHERE ownerId = :ownerId
    """)
    suspend fun balanceForOwner(ownerId: Long): Long

    /** Saldo fino a prima di 'before' (millisecondi epoch, esclusivo) */
    @Query("""
        SELECT COALESCE(SUM(incomeCents - expenseCents), 0)
        FROM Txn
        WHERE ownerId = :ownerId AND dateMillis < :before
    """)
    suspend fun balanceBefore(ownerId: Long, before: Long): Long
}
