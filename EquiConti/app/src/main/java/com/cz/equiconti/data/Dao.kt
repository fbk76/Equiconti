package com.cz.equiconti.data

import androidx.room.*

@Dao
interface OwnerDao {
    @Query("SELECT * FROM Owner ORDER BY surname, name")
    suspend fun listOwners(): List<Owner>

    @Insert
    suspend fun insert(owner: Owner): Long

    @Update
    suspend fun update(owner: Owner)

    @Delete
    suspend fun delete(owner: Owner)

    @Transaction
    @Query("SELECT * FROM Owner WHERE ownerId = :id")
    suspend fun loadWithHorses(id: Long): OwnerWithHorses?
}

@Dao
interface HorseDao {
    @Query("SELECT * FROM Horse WHERE ownerId = :ownerId ORDER BY name")
    suspend fun listByOwner(ownerId: Long): List<Horse>

    @Insert
    suspend fun insert(horse: Horse): Long

    @Update
    suspend fun update(horse: Horse)

    @Delete
    suspend fun delete(horse: Horse)
}

@Dao
interface TxnDao {
    @Query("SELECT * FROM Txn WHERE ownerId = :ownerId ORDER BY date, txnId")
    suspend fun listByOwner(ownerId: Long): List<Txn>

    @Insert
    suspend fun insert(txn: Txn): Long

    @Update
    suspend fun update(txn: Txn)

    @Delete
    suspend fun delete(txn: Txn)

    @Query("SELECT SUM(incomeCents - expenseCents) FROM Txn WHERE ownerId = :ownerId")
    suspend fun balanceForOwner(ownerId: Long): Long?

    @Query("SELECT SUM(incomeCents - expenseCents) FROM Txn WHERE ownerId = :ownerId AND date < :fromDate")
    suspend fun balanceBefore(ownerId: Long, fromDate: String): Long?

    @Query("SELECT * FROM Txn WHERE ownerId = :ownerId AND date BETWEEN :fromDate AND :toDate ORDER BY date, txnId")
    suspend fun listInRange(ownerId: Long, fromDate: String, toDate: String): List<Txn>
}
