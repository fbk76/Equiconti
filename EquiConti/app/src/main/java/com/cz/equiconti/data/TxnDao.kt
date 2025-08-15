package com.cz.equiconti.data

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TxnDao {
    @Query("SELECT * FROM txns WHERE horseId = :horseId ORDER BY timestamp DESC")
    fun getTxns(horseId: Long): Flow<List<Txn>>
}
