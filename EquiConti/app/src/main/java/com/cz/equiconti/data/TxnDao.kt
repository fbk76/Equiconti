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

    // Filtra le transazioni di un owner passando dalla relazione horse -> owner
    @Query("""
        SELECT t.* 
        FROM txns AS t
        INNER JOIN horses AS h ON h.id = t.horseId
        WHERE h.ownerId = :ownerId
        ORDER BY t.date DESC
    """)
    fun getByOwner(ownerId: Long): Flow<List<Txn>>
}
