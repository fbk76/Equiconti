package com.cz.equiconti.data
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
@Dao
interface TxnDao {
    @Query("SELECT * FROM txns WHERE ownerId = :ownerId ORDER BY id DESC")
    fun getOwnerTxns(ownerId: Long): Flow<List<Txn>>
    @Insert suspend fun insert(txn: Txn): Long
}
