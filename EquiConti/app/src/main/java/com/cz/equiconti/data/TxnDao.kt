package com.cz.equiconti.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.RewriteQueriesToDropUnusedColumns
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface TxnDao {

    @Upsert
    suspend fun upsert(txn: Txn)

    @Delete
    suspend fun delete(txn: Txn)

    // Tutte le transazioni dei cavalli di un owner:
    // selezioniamo SOLO righe dalla tabella txns (mappatura più semplice per Room)
    @RewriteQueriesToDropUnusedColumns
    @Query("""
        SELECT * 
        FROM txns
        WHERE horseId IN (
            SELECT id FROM horses WHERE ownerId = :ownerId
        )
        ORDER BY date DESC
    """)
    fun getByOwner(ownerId: Long): Flow<List<Txn>>
}
