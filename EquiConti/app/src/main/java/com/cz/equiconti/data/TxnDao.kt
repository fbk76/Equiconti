package com.cz.equiconti.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TxnDao {

    @Query("SELECT * FROM txns WHERE horseId = :horseId ORDER BY timestamp DESC")
    fun getTxns(horseId: Long): Flow<List<Txn>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(txn: Txn): Long

    @Delete
    suspend fun delete(txn: Txn)
}
