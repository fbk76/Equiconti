package com.cz.equiconti.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface TxnDao {

    // READ
    @Query("SELECT * FROM txns WHERE horseId = :horseId ORDER BY timestamp DESC")
    fun getTxns(horseId: Long): Flow<List<Txn>>

    @Query("SELECT * FROM txns WHERE id = :id")
    suspend fun getById(id: Long): Txn?

    // WRITE
    @Upsert
    suspend fun upsert(txn: Txn)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(txn: Txn): Long

    @Delete
    suspend fun delete(txn: Txn)
}
