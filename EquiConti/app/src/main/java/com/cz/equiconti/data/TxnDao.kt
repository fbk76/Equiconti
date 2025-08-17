package com.cz.equiconti.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface TxnDao {

    @Upsert
    suspend fun upsert(txn: Txn)

    @Delete
    suspend fun delete(txn: Txn)

    @Query("SELECT * FROM txns WHERE ownerId = :ownerId ORDER BY date DESC")
    fun getByOwner(ownerId: Long): Flow<List<Txn>>
}
