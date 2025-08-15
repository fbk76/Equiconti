package com.cz.equiconti.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TxnDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(txn: Txn): Long

    @Query("SELECT * FROM txns WHERE horseId = :horseId ORDER BY timestamp DESC")
    fun forHorse(horseId: Long): Flow<List<Txn>>
}
