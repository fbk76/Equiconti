package com.cz.equiconti.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TxnDao {
    @Query("SELECT * FROM txn WHERE horseId = :horseId ORDER BY date DESC")
    fun getTxns(horseId: Long): Flow<List<Txn>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(txn: Txn)

    @Delete
    suspend fun delete(txn: Txn)
}
